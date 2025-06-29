package com.bichpormak.data;

import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class MeetingDataProvider {

    private final UserDataProvider userDataProvider;

    public MeetingDataProvider(UserDataProvider userDataProvider) {
        this.userDataProvider = userDataProvider;
    }

    public MeetingEntity getMeetingWithoutMembers() {

        UserEntity organizer = userDataProvider.getOrganizer();

        return MeetingEntity.builder()
                .name("Meeting without members")
                .organizer(organizer)
                .members(null)
                .startMeeting(OffsetDateTime.now())
                .build();

    }

    public MeetingEntity getMeetingWithOneMember() {

        UserEntity member = userDataProvider.getFirstMember();

        return MeetingEntity.builder()
                .name("Meeting with one member")
                .organizer(userDataProvider.getOrganizer())
                .members(List.of(member))
                .startMeeting(OffsetDateTime.now())
                .build();

    }

    public MeetingEntity getMeetingWithTwoMembers() {

        UserEntity firstMember = userDataProvider.getFirstMember();
        UserEntity secondMember = userDataProvider.getSecondMember();

        return MeetingEntity.builder()
                .name("Meeting with two members")
                .organizer(userDataProvider.getOrganizer())
                .members(List.of(firstMember, secondMember))
                .startMeeting(OffsetDateTime.now())
                .build();

    }

    public MeetingEntity getMeetingWithThreeMembers() {

        UserEntity firstMember = userDataProvider.getFirstMember();
        UserEntity secondMember = userDataProvider.getSecondMember();
        UserEntity thirdMember = userDataProvider.getThirdMember();


        return MeetingEntity.builder()
                .name("Meeting with three member")
                .organizer(userDataProvider.getOrganizer())
                .members(List.of(firstMember, secondMember, thirdMember))
                .startMeeting(OffsetDateTime.now())
                .build();

    }

    public List<MeetingEntity> getThreeMeetingsInTwoOfWhichOriginalUser(UserEntity currentUser) {
        
        UserEntity organizer = userDataProvider.getOrganizer();
        UserEntity firstMember = userDataProvider.getFirstMember();
        UserEntity secondMember = userDataProvider.getSecondMember();

        MeetingEntity firstMeetingWithOriginalUser = MeetingEntity.builder()
                .name("Lecture 0.1")
                .organizer(organizer)
                .members(List.of(currentUser))
                .startMeeting(OffsetDateTime.now())
                .build();
        
        MeetingEntity secondMeetingWithOriginalUser = MeetingEntity.builder()
                .name("Lecture 0.2")
                .organizer(organizer)
                .members(List.of(currentUser, firstMember, secondMember))
                .startMeeting(OffsetDateTime.now())
                .build();
        
        MeetingEntity thirdMeetingWithoutOriginalUser = MeetingEntity.builder()
                .name("Lecture 0.3")
                .organizer(organizer)
                .members(List.of(firstMember, secondMember))
                .startMeeting(OffsetDateTime.now())
                .build();
        
        return List.of(
                firstMeetingWithOriginalUser,
                secondMeetingWithOriginalUser,
                thirdMeetingWithoutOriginalUser);
        
    }

    public MeetingEntity getMeetingWithoutOriginalUser() {

        UserEntity organizer = userDataProvider.getOrganizer();

        return MeetingEntity.builder()
                .name("Lecoture 0.1")
                .organizer(organizer)
                .members(null)
                .startMeeting(OffsetDateTime.now())
                .build();

    }


}
