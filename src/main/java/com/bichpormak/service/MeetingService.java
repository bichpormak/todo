package com.bichpormak.service;

import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;

import java.time.OffsetDateTime;
import java.util.List;

public interface MeetingService {

    MeetingResponse createMeeting(CreateMeetingRequest meeting);
    List<MeetingEntity> getAllUserMeetings(UserEntity userEntity);
    List<MeetingEntity> getAllUserMeetingsWhereHeIsOrganizer(UserEntity userEntity);
    List<MeetingEntity> getAllUserMeetingsForSpecifiedPeriod(UserEntity userEntity,
                                                             OffsetDateTime startDate,
                                                             OffsetDateTime endDate);
    List<MeetingEntity> getUsersUpcomingMeetings(UserEntity userEntity);

}
