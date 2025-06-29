package com.bichpormak.repository;

import com.bichpormak.entity.MeetingEntity;
import com.bichpormak.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Integer> {

    @Query("SELECT m FROM MeetingEntity m WHERE m.organizer = :user OR :user MEMBER OF m.members")
    List<MeetingEntity> getAllUserMeetings(@Param("user") UserEntity userEntity);

    @Query("SELECT m FROM MeetingEntity m WHERE m.organizer = :user")
    List<MeetingEntity> getAllUserMeetingsWhereHeIsOrganizer(@Param("user") UserEntity userEntity);

}
