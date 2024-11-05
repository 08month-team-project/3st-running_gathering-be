package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import lombok.*;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@Getter
public class GatheringDetailResponse {

    // 유의: dto로 바로 받아오는 것으로 수정했기때문에, 만약 필드관련 수정 시 GatheringRepositoryCustomImpl - getGatheringDetailWithUserParticipation 도 수정 필요

    private boolean isParticipation;

    private Long organizerId;
    private String organizerNickname;
    private String organizerProfileUrl;

    private GatheringDetailContentResponse content;


}
