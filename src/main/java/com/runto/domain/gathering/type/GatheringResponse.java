package com.runto.domain.gathering.type;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.dto.GatheringMember;
import com.runto.domain.gathering.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GatheringResponse { // 다른 목록조회에서도 쓸 예정

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

    private List<String> memberProfileUrls;

    public static GatheringResponse from(Gathering gathering) {

        List<String> memberProfileUrls = gathering.getGatheringMembers().stream()
                .map(member -> member.getUser().getProfileImageUrl())
                .toList();

        return GatheringResponse.builder()
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
                .memberProfileUrls(memberProfileUrls)
                .gatheringType(gathering.getGatheringType())
                .build();
    }
}
