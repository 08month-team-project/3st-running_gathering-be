package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.domain.Gathering;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GatheringsMapResponse {

    private CoordinatesDto centerCoordinates;
    private Double radiusDistance;
    private int totalGatheringCount;
    private List<GatheringResponse> gatheringResponses;


    public static GatheringsMapResponse of(Double radiusDistance,
                                           BigDecimal x, BigDecimal y,
                                           List<Gathering> gatherings) {


        GatheringsMapResponse response = new GatheringsMapResponse();

        response.centerCoordinates = new CoordinatesDto(x, y);
        response.radiusDistance = radiusDistance;

        List<GatheringResponse> gatheringResponseList = new ArrayList<>();

        int count = 0;
        for (Gathering gathering : gatherings) {
            gatheringResponseList.add(GatheringResponse
                    .fromGatheringMap(gathering));
            count++;
        }

        response.gatheringResponses = gatheringResponseList;
        response.totalGatheringCount = count;
        return response;
    }

}
