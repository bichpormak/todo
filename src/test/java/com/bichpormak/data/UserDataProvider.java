package com.bichpormak.data;

import com.bichpormak.entity.UserEntity;
import com.bichpormak.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class UserDataProvider {

    private final UserRepository userRepository;

    public UserDataProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getOrganizer() {

        UserEntity organizer = UserEntity.builder()
                .name("Lev")
                .surname("Pankratov")
                .email(UUID.randomUUID() + "@gmail.com")
                .build();

        userRepository.save(organizer);

        return organizer;

    }

    public UserEntity getSecondOrganizer() {

        UserEntity organizer = UserEntity.builder()
                .name("Ashab")
                .surname("Tamaev")
                .email(UUID.randomUUID() + "@gmail.com")
                .build();

        userRepository.save(organizer);

        return organizer;

    }

    public UserEntity getFirstMember() {

        UserEntity member = UserEntity.builder()
                .name("Brad")
                .surname("Pitt")
                .email(UUID.randomUUID() + "@gmail.com")
                .build();

        userRepository.save(member);

        return member;

    }

    public UserEntity getSecondMember() {

        UserEntity member = UserEntity.builder()
                .name("Kristina")
                .surname("Orbakaite")
                .email(UUID.randomUUID() + "@gmail.com")
                .build();

        userRepository.save(member);

        return member;

    }

    public UserEntity getThirdMember() {

        UserEntity member = UserEntity.builder()
                .name("Olga")
                .surname("Buzova")
                .email(UUID.randomUUID() + "@gmail.com")
                .build();

        userRepository.save(member);

        return member;

    }

    public UserEntity getMemberForSearchMeetings() {

        UserEntity member = UserEntity.builder()
                .name("Search")
                .surname("Search")
                .email("search@gmail.com")
                .build();

        userRepository.save(member);

        return member;

    }

}
