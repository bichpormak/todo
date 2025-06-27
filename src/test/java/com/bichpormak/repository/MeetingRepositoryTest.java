package com.bichpormak.repository;

import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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



}
