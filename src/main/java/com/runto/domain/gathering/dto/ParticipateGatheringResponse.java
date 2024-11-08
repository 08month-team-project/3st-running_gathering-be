package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class ParticipateGatheringResponse {


    private Long gatheringId;


    public static ParticipateGatheringResponse from(Gathering gathering) {

        return ParticipateGatheringResponse.builder()
                .gatheringId(gathering.getId())
                .build();
    }
}
