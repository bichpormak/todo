package com.bichpormak.dto;

import com.bichpormak.entity.UserEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMeetingRequest {

    @NotBlank
    private String name;

    private List<UserEntity> members;

    @NotNull
    @Future
    private OffsetDateTime startMeeting;
    private OffsetDateTime endMeeting;
    private int duration;

}
