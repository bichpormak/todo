package com.bichpormak.service;

import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;

public interface MeetingService {

    MeetingResponse createMeeting(CreateMeetingRequest meeting);

}
