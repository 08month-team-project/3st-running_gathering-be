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
public class GatheringDetailResponse { // 이벤트 상세조회 시에도 사용될 예정

    private GatheringDetailContentResponse gatheringContentResponse;
    private List<GatheringMemberResponse> gatheringMembers;

    
    public static GatheringDetailResponse from(Gathering gathering) {

        return GatheringDetailResponse.builder()
                .gatheringContentResponse(GatheringDetailContentResponse.from(gathering))
                .gatheringMembers(GatheringMemberResponse.from(gathering.getGatheringMembers()))
                .build();
    }

}
