package com.bichpormak.repository;

import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;
    private MeetingEntity meetingEntity;

    @BeforeEach
    public void setUp() {

        userEntity = UserEntity.builder()
                .name("Lev")
                .surname("Pankratov")
                .email("example@gmail.com")
                .build();

        userRepository.save(userEntity);

        meetingEntity = MeetingEntity.builder()
                .name("Lecture 0.1")
                .organizer(userEntity)
                .members(null)
                .build();

    }


    @Test
    @DisplayName("Test save meeting")
    public void givenMeeting_whenSave_thenMeetingIsSaved() {

        // given & when
        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);

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
        meetingRepository.save(meetingEntity);

        String updatedName = "Lecture 0.2";

        MeetingEntity meetingToUpdate = meetingRepository.findById(meetingEntity.getId())
                .orElse(null);

        meetingToUpdate.setName(updatedName);

        // when
        MeetingEntity updatedMeeting = meetingRepository.save(meetingToUpdate);

        // then
        assertThat(updatedMeeting).isNotNull();
        assertThat(updatedMeeting.getName()).isEqualTo(updatedName);


    }

    @Disabled
    @ParameterizedTest
    @NullSource
    @DisplayName("Test is not update meeting after saved")
    public void givenMeetingWithoutNameToUpdate_whenSave_thenMeetingIsNotUpdated(String name) {

        // given
        meetingRepository.save(meetingEntity);
        String updatedName = null;

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
        meetingRepository.save(meetingEntity);

        // when
        MeetingEntity obtainedMeeting = meetingRepository.findById(meetingEntity.getId())
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
    @DisplayName("Test get all developers")
    public void givenThreeDevelopersAreStored_whenFindAll_thenAllDeveloperAreReturned() {

        //given
        MeetingEntity meeting1 = MeetingEntity.builder()
                .name("Lecture 0.1")
                .organizer(userEntity)
                .members(null)
                .build();

        MeetingEntity meeting2 = MeetingEntity.builder()
                .name("Lecture 0.2")
                .organizer(userEntity)
                .members(null)
                .build();

        MeetingEntity meeting3 = MeetingEntity.builder()
                .name("Lecture 0.3")
                .organizer(userEntity)
                .members(null)
                .build();

        meetingRepository.saveAll(List.of(meeting1, meeting2, meeting3));

        //when
        List<MeetingEntity> meetings = meetingRepository.findAll();

        // then
        assertThat(meetings.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("Test delete meeting")
    public void givenMeetingSaved_whenDeleteMeeting_thenMeetingRemoved() {

        // given
        meetingRepository.save(meetingEntity);
        // when
        meetingRepository.delete(meetingEntity);
        // then
        assertThat(meetingRepository.findById(meetingEntity.getId())).isEmpty();

    }

    @Test
    @DisplayName("Test delete non-existent meeting")
    public void givenEmptyRepository_whenDeleteMeeting_thenNothingWillHappen() {

        // given & when & then
        assertDoesNotThrow(() -> meetingRepository.deleteById(1));

    }



}
