package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
@Getter
public class GatheringDetailResponse { // 멤버목록 가져오기는 api 분리로 수정

    private GatheringDetailContentResponse gatheringContentResponse;

    
    public static GatheringDetailResponse fromGathering(Gathering gathering) {

        return GatheringDetailResponse.builder()
                .gatheringContentResponse(GatheringDetailContentResponse.from(gathering))
                .build();
    }

}
