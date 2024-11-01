package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GatheringsResponse {

    private GatheringsRequestParams gatheringsRequestParams;
    private Slice<GatheringResponse> gatheringResponses;

    public static GatheringsResponse fromGeneralGatherings(Slice<Gathering> gatherings, GatheringsRequestParams params) {
        GatheringsResponse response = new GatheringsResponse();

        response.gatheringsRequestParams = params;
        response.gatheringResponses = gatherings.map(GatheringResponse::fromGeneralGathering);

        return response;
    }

    public static GatheringsResponse fromEventGatherings(Slice<Gathering> gatherings, GatheringsRequestParams params) {

        GatheringsResponse response = new GatheringsResponse();

        response.gatheringsRequestParams = params;
        response.gatheringResponses = gatherings.map(GatheringResponse::fromEventGathering);

        return response;
    }
}
