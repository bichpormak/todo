package com.bichpormak.repository;

import com.bichpormak.data.UserDataProvider;
import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.data.MeetingDataProvider;
import com.bichpormak.entity.UserEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@Import({MeetingDataProvider.class, UserDataProvider.class})
public class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MeetingDataProvider meetingDataProvider;

    @Autowired
    private UserDataProvider userDataProvider;


    @Test
    @DisplayName("Test save meeting")
    public void givenMeeting_whenSave_thenMeetingIsSaved() {

        // given
        MeetingEntity meeting = meetingDataProvider.getMeetingWithoutMembers();

        // when
        MeetingEntity savedMeeting = meetingRepository.save(meeting);

        // then
        assertThat(savedMeeting).isNotNull();
        assertThat(savedMeeting.getId()).isNotNull();

    }

    @Test
    @DisplayName("Test is not save meeting")
    public void givenMeetingIsNull_whenSave_thenMeetingIsNotCreated() {

        assertThrows(RuntimeException.class, () -> meetingRepository.save(null));

    }

    @Test
    @DisplayName("Test update meeting after saved")
    public void givenMeetingToUpdate_whenSave_thenMeetingIsChanged() {

        //given
        MeetingEntity meeting = meetingDataProvider.getMeetingWithoutMembers();

        meetingRepository.save(meeting);

        String updatedName = "Lecture 0.2";

        MeetingEntity meetingToUpdate = meetingRepository.findById(meeting.getId())
                .orElse(null);

        meetingToUpdate.setName(updatedName);

        // when
        MeetingEntity updatedMeeting = meetingRepository.save(meetingToUpdate);

        // then
        assertThat(updatedMeeting).isNotNull();
        assertThat(updatedMeeting.getName()).isEqualTo(updatedName);


    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test is not update meeting after saved")
    public void givenMeetingWithoutNameToUpdate_whenSave_thenMeetingIsNotUpdated(String updatedName) {

        // given
        MeetingEntity meeting = meetingDataProvider.getMeetingWithoutMembers();

        meetingRepository.save(meeting);

        MeetingEntity meetingToUpdate = meetingRepository.findById(1)
                .orElse(null);

        meetingToUpdate.setName(updatedName);

        // when
        MeetingEntity updatedMeeting = meetingRepository.save(meetingToUpdate);

        //then
        assertThat(updatedMeeting).isNull();

    }

    @Test
    @DisplayName("Test get meeting by id")
    public void givenMeetingCreated_whenGetById_thenMeetingIsReturned() {

        // given
        MeetingEntity meeting = meetingDataProvider.getMeetingWithoutMembers();

        meetingRepository.save(meeting);

        // when
        MeetingEntity obtainedMeeting = meetingRepository.findById(meeting.getId())
                .orElse(null);

        //then
        assertThat(obtainedMeeting).isNotNull();

    }

    @Test
    @DisplayName("Test developer not found")
    public void givenMeetingIsNotCreated_whenGetById_thenOptionalIsNull() {

        // given & when
        MeetingEntity obtainedMeeting = meetingRepository.findById(1).orElse(null);

        // then
        assertThat(obtainedMeeting).isNull();

    }

    @Test
    @DisplayName("Test get all meetings")
    public void givenThreeMeetingsAreStored_whenFindAll_thenAllMeetingsAreReturned() {

        //given
        MeetingEntity firstMeeting = meetingDataProvider.getMeetingWithOneMember();
        MeetingEntity secondMeeting = meetingDataProvider.getMeetingWithTwoMembers();
        MeetingEntity thirdMeeting = meetingDataProvider.getMeetingWithThreeMembers();

        meetingRepository.saveAll(List.of(firstMeeting, secondMeeting, thirdMeeting));

        //when
        List<MeetingEntity> meetings = meetingRepository.findAll();

        // then
        assertThat(meetings.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("Test delete meeting")
    public void givenMeetingSaved_whenDeleteMeeting_thenMeetingRemoved() {

        // given
        MeetingEntity meeting = meetingDataProvider.getMeetingWithoutMembers();
        meetingRepository.save(meeting);

        // when
        meetingRepository.delete(meeting);

        // then
        assertThat(meetingRepository.findById(meeting.getId())).isEmpty();

    }

    @Test
    @DisplayName("Test delete non-existent meeting")
    public void givenEmptyRepository_whenDeleteMeeting_thenNothingWillHappen() {

        // given & when & then
        assertDoesNotThrow(() -> meetingRepository.deleteById(1));

    }

    @Test
    @DisplayName("Test search all user meetings")
    public void givenUser_whenGetAllUserMeetings_thenReturnAllMeetings() {

        // given
        UserEntity member = userDataProvider.getMemberForSearchMeetings();

        meetingRepository.saveAll(
                meetingDataProvider.getThreeMeetingsInTwoOfWhichOriginalUser(member));

        // when
        List<MeetingEntity> meetingsOfUser = meetingRepository.getAllUserMeetings(member);

        //then
        int countMeetingsOfWhichOriginalUser = 2;

        assertThat(meetingsOfUser.size())
                .isEqualTo(countMeetingsOfWhichOriginalUser);

    }

    @Test
    @DisplayName("Test search user without meetings")
    public void givenUserWithoutMeetings_whenGetAllUserMeetings_thenReturnEmptyList() {

        // given
        UserEntity member = userDataProvider.getMemberForSearchMeetings();

        meetingRepository.save(meetingDataProvider.getMeetingWithoutOriginalUser());

        //when
        List<MeetingEntity> meetingsOfUser = meetingRepository.getAllUserMeetings(member);

        // then
        int countUserMeetings = 0; // because we didn't add the user anywhere

        assertThat(meetingsOfUser.isEmpty()).isTrue();


    }

    @Test
    @DisplayName("Test search non-existent user meetings")
    public void givenNonExistentUser_whenGetAllUserMeetings_thenReturnEmptyList() {

        // given
        UserEntity member = null;

        meetingRepository.save(meetingDataProvider.getMeetingWithoutOriginalUser());

        // when
        List<MeetingEntity> meetingsOfUser = meetingRepository.getAllUserMeetings(member);

        //then
        int countMeetingsOfWhichOriginalUser = 0;

        assertThat(meetingsOfUser.size())
                .isEqualTo(countMeetingsOfWhichOriginalUser);

    }

    @Disabled("needs to be fixed")
    @Test
    @DisplayName("Test search meetings, where user is organizer")
    public void givenUser_whenGetAllUserMeetingsWhereHeIsOrganizer_thenReturnAllMeetings() {

        // given
        UserEntity organizer = userDataProvider.getOrganizer();

        MeetingEntity firstMeeting = meetingDataProvider.getMeetingWithOneMember();
        MeetingEntity secondMeeting = meetingDataProvider.getMeetingWithTwoMembers();
        MeetingEntity thirdMeeting = meetingDataProvider.getMeetingWithThreeMembers();

        meetingRepository.saveAll(List.of(firstMeeting, secondMeeting, thirdMeeting));

        // when
        List<MeetingEntity> meetingsOfOrganizer =
                meetingRepository.getAllUserMeetingsWhereHeIsOrganizer(organizer);

        // then
        int countMeetingWhereUserIsOrganizer = 3; // in all meetings this user is the organizer

        assertThat(meetingsOfOrganizer.size())
                .isEqualTo(countMeetingWhereUserIsOrganizer);

    }

}