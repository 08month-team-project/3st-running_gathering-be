package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.GatheringStatus;
import com.runto.domain.gathering.type.GatheringType;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static com.runto.domain.gathering.type.GatheringStatus.REPORTED;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime appointedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
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

    // 이벤트 모임목록 조회, 지도기반 일반모임목록조회엔 null 값으로 내보냄
    private List<String> memberProfileUrls;

    public static GatheringResponse fromGeneralGathering(Gathering gathering) {

        List<String> memberProfileUrls = gathering.getGatheringMembers().stream()
                .map(member -> member.getUser().getProfileImageUrl())
                .toList();

        // 신고상태면 썸네일은 내보내지 않음
        String thumbnailUrl = null;
        if(!REPORTED.equals(gathering.getStatus())){
            thumbnailUrl = gathering.getThumbnailUrl();
        }

        return GatheringResponse.builder()
                .id(gathering.getId())
                .organizerId(gathering.getOrganizerId())
                .title(gathering.getTitle())
                .appointedAt(gathering.getAppointedAt())
                .deadline(gathering.getDeadline())
                .concept(gathering.getConcept())
                .goalDistance(gathering.getGoalDistance())
                .thumbnailUrl(thumbnailUrl)
                .hits(gathering.getHits())
                .location(LocationDto.from(gathering.getLocation()))
                .status(gathering.getStatus())
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .memberProfileUrls(memberProfileUrls)
                .gatheringType(gathering.getGatheringType())
                .build();
    }

    public static GatheringResponse fromEventGathering(Gathering gathering) {

        // 신고상태면 썸네일은 내보내지 않음
        String thumbnailUrl = null;
        if(!REPORTED.equals(gathering.getStatus())){
            thumbnailUrl = gathering.getThumbnailUrl();
        }

        return GatheringResponse.builder()
                .id(gathering.getId())
                .organizerId(gathering.getOrganizerId())
                .title(gathering.getTitle())
                .appointedAt(gathering.getAppointedAt())
                .deadline(gathering.getDeadline())
                .concept(gathering.getConcept())
                .goalDistance(gathering.getGoalDistance())
                .thumbnailUrl(thumbnailUrl)
                .hits(gathering.getHits())
                .location(LocationDto.from(gathering.getLocation()))
                .status(gathering.getStatus())
                .maxNumber(gathering.getMaxNumber())
                .currentNumber(gathering.getCurrentNumber())
                .gatheringType(gathering.getGatheringType())
                .build();
    }

    public static GatheringResponse fromGatheringMap(Gathering gathering) {
        
        // 처음부터 현재 참가 가능한 반경 내 모임만 가져올 것임

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
                .gatheringType(gathering.getGatheringType())
                .build();
    }
}
