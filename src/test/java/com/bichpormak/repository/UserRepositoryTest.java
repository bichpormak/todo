package com.bichpormak.repository;

import com.bichpormak.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test save user")
    public void givenUser_whenSave_thenUserIsSaved() {

        UserEntity userEntity = UserEntity.builder()
                .name("Lev")
                .surname("Pankratov")
                .email("example@gmail.com")
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntity);

        assertThat(savedUserEntity).isNotNull();
        assertThat(savedUserEntity.getId()).isNotNull();

    }


}
