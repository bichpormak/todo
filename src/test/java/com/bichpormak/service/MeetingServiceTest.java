package com.bichpormak.service;

import com.bichpormak.config.MeetingConfig;
import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import com.bichpormak.repository.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MeetingServiceTest {

    @Mock
    MeetingRepository meetingRepository;

    @Mock
    private MeetingConfig meetingConfig;

    @InjectMocks
    MeetingServiceImpl meetingService;

    MeetingEntity meeting;

    CreateMeetingRequest request;

    @BeforeEach
    public void setUp() {

        UserEntity organizer = UserEntity.builder()
                .name("Lev")
                .surname("Pankratov")
                .email("example@gmail.com")
                .build();

        UserEntity member = UserEntity.builder()
                .name("Egor")
                .surname("Frolov")
                .email("example2@gmail.com")
                .build();

        meeting = MeetingEntity.builder()
                .name("Lecture 0.1")
                .organizer(organizer)
                .members(List.of(member))
                .startMeeting(OffsetDateTime.now())
                .endMeeting(OffsetDateTime.now().plusHours(1))
                .duration(60)
                .build();

        request = CreateMeetingRequest.builder()
                .name(meeting.getName())
                .organizer(meeting.getOrganizer())
                .members(meeting.getMembers())
                .startMeeting(meeting.getStartMeeting())
                .endMeeting(meeting.getEndMeeting())
                .build();

    }

    public void mockProperties(int minDuration, int maxDuration, int maxMembers) {

        when(meetingConfig.getMinDurationMeeting()).thenReturn(minDuration);
        when(meetingConfig.getMaxDurationMeeting()).thenReturn(maxDuration);
        when(meetingConfig.getMaxMembersInOneMeeting()).thenReturn(maxMembers);

    }


}
