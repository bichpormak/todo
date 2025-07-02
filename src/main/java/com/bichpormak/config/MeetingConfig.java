package com.bichpormak.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MeetingConfig {

    @Value("${meeting.restriction.max-members-in-one-meeting}")
    private int maxMembersInOneMeeting;

    @Value("${meeting.restriction.min-duration-meeting}")
    private int minDurationMeeting;

    @Value("${meeting.restriction.max-duration-meeting}")
    private int maxDurationMeeting;

}
