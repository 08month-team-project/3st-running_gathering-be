package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.GatheringStatus;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
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
public class GatheringDetailContentResponse { // 이벤트 상세조회 시에도 사용될 예정

    private Long id;

    private Long organizerId;

    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    private RunningConcept concept;

    private GoalDistance goalDistance;

    private Long hits;

    private LocationDto location;

    private GatheringStatus status;

    private Integer maxNumber;

    private Integer currentNumber;

    public static GatheringDetailContentResponse from(Gathering gathering) {
        return GatheringDetailContentResponse.builder()
                .id(gathering.getId())
                .organizerId(gathering.getOrganizerId())
                .title(gathering.getTitle())
                .description(gathering.getDescription())
                .appointedAt(gathering.getAppointedAt())
                .deadline(gathering.getDeadline())
                .concept(gathering.getConcept())
                .goalDistance(gathering.getGoalDistance())
                .hits(gathering.getHits())
                .location(LocationDto.from(gathering.getLocation()))
                .status(gathering.getStatus())
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .build();
    }
}
