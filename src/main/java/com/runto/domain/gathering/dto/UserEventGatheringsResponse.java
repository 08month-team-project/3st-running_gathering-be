package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.EventGathering;
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
public class UserEventGatheringsResponse {

    private Slice<EventGatheringResponse> userGatheringResponses;

    public static UserEventGatheringsResponse from(Slice<EventGathering> eventGatherings) {

        return UserEventGatheringsResponse.builder()
                .userGatheringResponses(eventGatherings.map(EventGatheringResponse::from))
                .build();
    }

}
