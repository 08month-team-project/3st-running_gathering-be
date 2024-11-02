package com.runto.domain.admin.dto;

import lombok.Getter;

@Getter
public class GatheringCountResponse {

    private String gathering;

    private Long count;

    public GatheringCountResponse(String gathering, Long count) {
        this.gathering = gathering;
        this.count = count;
    }

}
