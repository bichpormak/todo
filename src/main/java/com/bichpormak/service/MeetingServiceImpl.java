package com.bichpormak.service;

import com.bichpormak.config.MeetingConfig;
import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import com.bichpormak.exception.*;
import com.bichpormak.repository.MeetingRepository;
import com.bichpormak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingConfig meetingConfig;

    public MeetingResponse createMeeting(CreateMeetingRequest meeting) {

        checkOrganizerIsNotAmongMembers(meeting.getOrganizer(), meeting.getMembers());
        validateTimeParameters(meeting.getEndMeeting(), meeting.getDuration());
        checkingStartIsNotInPast(meeting.getStartMeeting());

        final OffsetDateTime endMeeting =
                getEndMeetingTime(meeting.getStartMeeting(), meeting.getEndMeeting(), meeting.getDuration());

        final Integer duration = getDuration(meeting.getStartMeeting(), endMeeting);

        checkEndDateLaterThanStartDate(meeting.getStartMeeting(), endMeeting);
        checkingMeetingCreationRules(meeting.getMembers(), duration);

        MeetingEntity meetingToSave = buildMeetingFromRequest(meeting, endMeeting, duration);

        MeetingEntity savedMeeting = meetingRepository.save(meetingToSave);

        doLogAboutSaveMeeting(savedMeeting);

        return buildResponseFromMeetingEntity(savedMeeting);

    }

    private void checkOrganizerIsNotAmongMembers(UserEntity organizer,
                                                 List<UserEntity> members) {

        boolean organizerInListMembers = members.contains(organizer);

        if (organizerInListMembers) {
            log.error("Failed meeting creation: organizer in list members");
            throw new OrganizerInListMembers("The organizer is among the participants");
        }

    }

    private void validateTimeParameters(OffsetDateTime endMeeting, Integer duration) {

        if (endMeeting == null && duration == null || endMeeting != null && duration != null) {

            log.error("Failed meeting creation: set both the end of the meeting and the duration");
            throw new TimeParametersException("Conveyed both the end of the meeting and its duration");

        }

    }

    private void checkingStartIsNotInPast(OffsetDateTime startMeeting) {

        int errorSeconds = 5;

        if (startMeeting.isBefore(OffsetDateTime.now().minusSeconds(errorSeconds))) {

            log.error("Failed meeting creation: start in the past");
            throw new TimeParametersException("Start in the past");

        }

    }

    private void checkingMeetingCreationRules(List<UserEntity> members, int duration) {

        if (members.size() > meetingConfig.getMaxMembersInOneMeeting()) {

            log.error("Failed meeting creation: more participants than maximum");
            throw new ManyMembersException("More participants than maximum");

        }

        if (duration < meetingConfig.getMinDurationMeeting() ||
                duration > meetingConfig.getMaxDurationMeeting()) {

            log.error("Failed meeting creation: the duration of the meeting does not meet the restrictions");
            throw new DurationMeetingException("The duration of the meeting does not meet the restrictions");

        }

    }

    private void checkEndDateLaterThanStartDate(OffsetDateTime startMeeting, OffsetDateTime endMeeting) {

        if (endMeeting.isBefore(startMeeting)) {

            log.error("Failed meeting creation: end of meeting before start");
            throw new TimeParametersException("End of meeting before start");

        }

    }

    private OffsetDateTime getEndMeetingTime(OffsetDateTime startMeeting,
                                             OffsetDateTime endMeeting,
                                             Integer duration) {

        return duration != null ? startMeeting.plusMinutes(duration) : endMeeting;

    }

    private Integer getDuration(OffsetDateTime startMeeting, OffsetDateTime endMeeting) {

        return (int) Duration.between(startMeeting, endMeeting).toMinutes();

    }

    private void doLogAboutSaveMeeting(MeetingEntity meeting) {

        log.info("Created meeting id={} : organizer={}, members={}",
                meeting.getId(),
                meeting.getOrganizer(),
                meeting.getMembers().size());


    }

    private MeetingEntity buildMeetingFromRequest(CreateMeetingRequest request, OffsetDateTime endMeeting, int duration) {

        return MeetingEntity.builder()
                .name(request.getName())
                .organizer(request.getOrganizer())
                .members(request.getMembers())
                .startMeeting(request.getStartMeeting())
                .endMeeting(endMeeting)
                .duration(duration)
                .build();

    }

    private MeetingResponse buildResponseFromMeetingEntity(MeetingEntity meeting) {

        return MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .organizer(meeting.getOrganizer())
                .members(meeting.getMembers())
                .createAt(meeting.getCreatedAt())
                .startMeeting(meeting.getStartMeeting())
                .endMeeting(meeting.getEndMeeting())
                .duration(meeting.getDuration())
                .build();

    }

    @Override
    public List<MeetingEntity> getAllUserMeetings(UserEntity userEntity) {

        checkingUserExistence(userEntity);

        List<MeetingEntity> meetings = meetingRepository.getAllUserMeetings(userEntity);

        for (MeetingEntity meeting : meetings) {
            log.info("Get meeting: {}", meeting);
        }

        return meetings;

    }

    private void checkingUserExistence(UserEntity userEntity) {

        userRepository.findById(userEntity.getId())
                .orElseThrow(() -> {

                    log.error("Failed to get meetings: user not found");
                    return new UserNotFoundException("User not found");

                });

    }

    @Override
    public List<MeetingEntity> getAllUserMeetingsWhereHeIsOrganizer(UserEntity userEntity) {

        checkingUserExistence(userEntity);

        List<MeetingEntity> meetings =
                meetingRepository.getAllUserMeetingsWhereHeIsOrganizer(userEntity);

        for (MeetingEntity meeting : meetings) {
            log.info("Get meeting: {}", meetings);
        }

        return meetings;

    }

    @Override
    public List<MeetingEntity> getAllUserMeetingsForSpecifiedPeriod(UserEntity userEntity, OffsetDateTime startDate, OffsetDateTime endDate) {

        checkingUserExistence(userEntity);
        checkEndDateLaterThanStartDate(startDate, endDate);

        List<MeetingEntity> meetings =
                meetingRepository.getAllUserMeetingsForSpecifiedPeriod(userEntity, startDate, endDate);

        for (MeetingEntity meeting : meetings) {
            log.info("Get meeting: {}", meeting);
        }

        return meetings;

    }

    @Override
    public List<MeetingEntity> getUsersUpcomingMeetings(UserEntity userEntity) {

        checkingUserExistence(userEntity);

        List<MeetingEntity> meetings = meetingRepository.getUsersUpcomingMeetings(userEntity);

        for (MeetingEntity meeting : meetings) {
            log.info("Get meeting: {}", meeting);
        }

        return meetings;

    }

}
