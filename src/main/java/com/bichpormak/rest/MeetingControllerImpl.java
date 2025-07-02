package com.bichpormak.rest;


import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
public class MeetingControllerImpl implements MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody CreateMeetingRequest request) {

        final MeetingResponse meetingResponse = meetingService.createMeeting(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingResponse);

    }

}
