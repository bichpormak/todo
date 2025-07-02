package com.bichpormak.service;


import com.bichpormak.data.MeetingDataProvider;
import com.bichpormak.data.UserDataProvider;
import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.mapper.MeetingMapper;
import com.bichpormak.repository.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import(MeetingDataProvider.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingDataProvider meetingDataProvider;

    @InjectMocks
    private MeetingService meetingService;

    @Test
    @DisplayName("Test creation valid meetings")
    public void givenMeetingToCreate_whenCreateMeeting_givenMeetingSavedSuccessful() {

        MeetingEntity meeting = meetingDataProvider.getMeetingWithOneMember();

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .name(meeting.getName())
                .organizer(meeting.getOrganizer())
                .startMeeting(meeting.getStartMeeting())
                .endMeeting(meeting.getEndMeeting())
                .build();

        MeetingResponse response = meetingService.createMeeting(request);

        assertThat(response)
                .isNotNull();

    }


}
