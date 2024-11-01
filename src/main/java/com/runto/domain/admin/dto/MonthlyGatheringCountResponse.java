package com.runto.domain.admin.dto;

import lombok.Getter;

@Getter
public class MonthlyGatheringCountResponse {

    private String gathering;

    private Long count;

    public MonthlyGatheringCountResponse(String gathering, Long count) {
        this.gathering = gathering;
        this.count = count;
    }

}
