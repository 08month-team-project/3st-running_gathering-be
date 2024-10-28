package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@Getter
public class GatheringDetailResponse {

    private GatheringResponse gatheringResponse;
    private List<GatheringMemberResponse> gatheringMembers;


    public static GatheringDetailResponse from(Gathering gathering) { // 패치 조인 해온 모임글

        return GatheringDetailResponse.builder()
                .gatheringResponse(GatheringResponse.from(gathering))
                .gatheringMembers(GatheringMemberResponse.from(gathering.getGatheringMembers()))
                .build();
    }

}
