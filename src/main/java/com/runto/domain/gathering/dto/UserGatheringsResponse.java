package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.GatheringResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserGatheringsResponse {

    private Slice<GatheringResponse> userGatheringResponses;

    public static UserGatheringsResponse fromGatherings(Slice<Gathering> gatherings) {

        return UserGatheringsResponse.builder()
                .userGatheringResponses(gatherings.map(GatheringResponse::from))
                .build();
    }

}
