package com.runto.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.GatheringMember;
import com.runto.domain.gathering.type.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CalendarGatheringResponse {

    private Long id;
    private Long organizerId;
    private LocalDateTime appointedAt;
    private LocalDateTime deadline;
    private RunningConcept concept;
    private GoalDistance goalDistance;
    private Integer maxNumber;
    private Integer currentNumber;
    private GatheringStatus status;
    private GatheringType gatheringType;

    // memberId 로 하면 GatheringMember의 pk 값 같아서 헷갈릴 소지가 있기때문에 이렇게 명명하였음
    private Long memberAccountId;
    private GatheringMemberRole role;
    private AttendanceStatus attendanceStatus;
    private Double realDistance;


    public static CalendarGatheringResponse from(GatheringMember member) { // 패치조인해온 gatheringMember

        Gathering gathering = member.getGathering();

        return CalendarGatheringResponse.builder()
                .id(gathering.getId())
                .organizerId(gathering.getOrganizerId())
                .appointedAt(gathering.getAppointedAt())
                .deadline(gathering.getDeadline())
                .concept(gathering.getConcept())
                .goalDistance(gathering.getGoalDistance())
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .status(gathering.getStatus())
                .gatheringType(gathering.getGatheringType())
                .memberAccountId(member.getUser().getId())
                .role(member.getRole())
                .attendanceStatus(member.getAttendanceStatus())
                .realDistance(member.getRealDistance())
                .build();
    }
}
