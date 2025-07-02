package com.bichpormak.dto;

import com.bichpormak.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MeetingResponse {

    @NotNull
    private int id;
    @NotBlank
    private String name;
    @NotNull
    private UserEntity organizer;
    @NotNull
    private List<UserEntity> members;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @NotNull
    private OffsetDateTime createAt;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @NotNull
    private OffsetDateTime startMeeting;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @NotNull
    private OffsetDateTime endMeeting;
    @NotNull
    private int duration;

}
