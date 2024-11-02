package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttendanceEventGatheringResponse { // 수정될 수도 있음

    private Double realDistance;
    private long requestAttendingMemberCount;
    private long updateAttendingMemberCount;
    private long notAttendingMemberCount;
}
