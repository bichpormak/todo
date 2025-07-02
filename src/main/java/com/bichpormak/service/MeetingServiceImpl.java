package com.bichpormak.service;

import com.bichpormak.config.MeetingConfig;
import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import com.bichpormak.exception.DurationMeetingException;
import com.bichpormak.exception.ManyMembersException;
import com.bichpormak.exception.OrganizerInListMembers;
import com.bichpormak.exception.TimeParametersException;
import com.bichpormak.mapper.MeetingMapper;
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
    private final MeetingMapper meetingMapper;

    public MeetingResponse createMeeting(CreateMeetingRequest meeting) {

        checkOrganizerIsAmongMembers(meeting.getOrganizer(), meeting.getMembers());
        validateTimeParameters(meeting.getEndMeeting(), meeting.getDuration());

        final OffsetDateTime endTime =
                getEndMeetingTime(meeting.getStartMeeting(), meeting.getEndMeeting(), meeting.getDuration());

        final Integer duration = getDuration(meeting.getStartMeeting(), endTime);

        checkingMeetingCreationRules(meeting.getMembers(), duration);
        checkEndDateLaterThanStartDate(meeting.getStartMeeting(), endTime);

        MeetingEntity meetingToSave = meetingMapper.map(meeting);
        meetingToSave.setEndMeeting(endTime);
        meetingToSave.setDuration(duration);

        MeetingEntity savedMeeting = meetingRepository.save(meetingToSave);

        return meetingMapper.map(savedMeeting);

    }

    private void checkOrganizerIsAmongMembers(UserEntity organizer,
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

}
