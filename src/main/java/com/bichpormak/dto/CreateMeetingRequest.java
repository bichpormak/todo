package com.bichpormak.dto;

import com.bichpormak.entity.UserEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CreateMeetingRequest {

    @NotBlank
    private String name;

    @NotNull
    private UserEntity organizer;
    private List<UserEntity> members;

    @NotNull
    @Future
    private OffsetDateTime startMeeting;
    private OffsetDateTime endMeeting;
    private Integer duration;

}
