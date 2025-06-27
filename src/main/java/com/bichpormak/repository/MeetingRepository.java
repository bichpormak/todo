package com.bichpormak.repository;

import com.bichpormak.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Integer> {

}
