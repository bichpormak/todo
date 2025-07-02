package com.bichpormak.mapper;

import com.bichpormak.dto.CreateMeetingRequest;
import com.bichpormak.dto.MeetingResponse;
import com.bichpormak.entity.MeetingEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    MeetingResponse map(MeetingEntity meeting);

    @InheritInverseConfiguration
    MeetingEntity map(CreateMeetingRequest meeting);

}
