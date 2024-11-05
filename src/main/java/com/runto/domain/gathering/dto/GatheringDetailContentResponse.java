package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.type.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class GatheringDetailContentResponse { // 이벤트 상세조회 시에도 사용될 예정

    // 유의: dto로 바로 받아오는것으로 수정했기때문에, 만약 필드관련 수정 시 GatheringRepositoryCustomImpl - getGatheringDetailWithUserParticipation 도 수정 필요
    private Long id;

    private GatheringType type;

    //private Long organizerId;

    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    private RunningConcept concept;

    private GoalDistance goalDistance;

    private Long hits;

    private String addressFullName;

    private CoordinatesDto coordinates;

    private GatheringStatus status;

    private Integer maxNumber;

    private Integer currentNumber;

    private EventRequestStatus eventRequestStatus; // 일반모임이면 해당 값은 null

}
