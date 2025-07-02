package com.bichpormak.service;

import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.exception.DurationMeetingException;
import com.bichpormak.exception.ManyMembersException;
import com.bichpormak.exception.OrganizerInListMembers;
import com.bichpormak.exception.TimeParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateMeetingTest extends MeetingServiceTest {

    @Test
    @DisplayName("Test creation valid meetings")
    public void givenMeetingToCreate_whenCreateMeeting_givenMeetingSavedSuccessful() {

        mockProperties(15, 120, 50);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        MeetingResponse response = meetingService.createMeeting(request);

        MeetingResponse expectedResponse = MeetingResponse.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .organizer(meeting.getOrganizer())
                .members(meeting.getMembers())
                .createAt(meeting.getCreatedAt())
                .startMeeting(meeting.getStartMeeting())
                .endMeeting(meeting.getEndMeeting())
                .duration(meeting.getDuration())
                .build();

        assertThat(response.equals(expectedResponse)).isTrue();

        verify(meetingRepository).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test to save meeting with exceeded number of members")
    public void givenMeetingWithExceededNumberMembers_whenCreateMeeting_thenMeetingIsNotSaved() {

        mockProperties(15 ,30, 0);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        assertThrows(ManyMembersException.class,() -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test save meeting with short duration")
    public void givenMeetingWithShortDuration_whenCreateMeeting_givenMeetingIsNotSaved() {

        mockProperties(300, 400, 1);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        assertThrows(DurationMeetingException.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test save meeting with long duration")
    public void givenMeetingWithLongDuration_whenCreateMeeting_givenMeetingIsNotSaved() {

        mockProperties(1, 10, 1);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        assertThrows(DurationMeetingException.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("test save meeting with end that is before the beginning")
    public void givenMeetingWithEndBeforeStart_whenCreateMeeting_thenMeetingIsNotSaved() {

        mockProperties(10, 120, 15);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        request.setStartMeeting(OffsetDateTime.now());
        request.setEndMeeting(OffsetDateTime.now().minusHours(1));

        assertThrows(TimeParametersException.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test to save a meeting with both end and duration set")
    public void givenMeetingWithEndAndDuration_whenCreateMeeting_givenMeetingIsNotCreated() {

        mockProperties(10, 120, 15);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        request.setDuration(meeting.getDuration());

        assertThrows(TimeParametersException.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test save meeting with start in the past")
    public void givenMeetingWithStartInThePast_whenCreateMeeting_thenMeetingIsNotCreated() {

        mockProperties(10, 120, 15);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        request.setStartMeeting(OffsetDateTime.now().minusMinutes(1));

        assertThrows(TimeParametersException.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));

    }

    @Test
    @DisplayName("Test save meeting with organizer in list members")
    public void givenMeetingWithOrganizerInMembers_whenCreateMeeting_thenMeetingIsNotCreated() {

        mockProperties(10, 120, 15);

        when(meetingRepository.save(any(MeetingEntity.class))).thenReturn(meeting);

        request.setMembers(List.of(meeting.getOrganizer()));

        assertThrows(OrganizerInListMembers.class, () -> meetingService.createMeeting(request));

        verify(meetingRepository, never()).save(any(MeetingEntity.class));


    }




}
