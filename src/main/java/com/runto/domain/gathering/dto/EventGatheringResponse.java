package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.EventGathering;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventGatheringResponse {

    private Long id;
    private Long organizerId;
    private String title;
    private LocalDateTime appointedAt;
    private LocalDateTime deadline;
    private RunningConcept concept;
    private GoalDistance goalDistance;
    private String thumbnailUrl;
    private Long hits;
    private LocationDto location;
    private GatheringStatus status;
    private Integer maxNumber;
    private Integer currentNumber;
    private GatheringType gatheringType;

    private EventRequestStatus requestStatus;

    public static EventGatheringResponse from(EventGathering eventGathering) {

        Gathering gathering = eventGathering.getGathering();

        return EventGatheringResponse.builder()
                .id(gathering.getId())
                .organizerId(gathering.getOrganizerId())
                .title(gathering.getTitle())
                .appointedAt(gathering.getAppointedAt())
                .deadline(gathering.getDeadline())
                .concept(gathering.getConcept())
                .goalDistance(gathering.getGoalDistance())
                .thumbnailUrl(gathering.getThumbnailUrl())
                .hits(gathering.getHits())
                .location(LocationDto.from(gathering.getLocation()))
                .status(gathering.getStatus())
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .gatheringType(gathering.getGatheringType())
                .requestStatus(eventGathering.getStatus())
                .build();
    }
}
