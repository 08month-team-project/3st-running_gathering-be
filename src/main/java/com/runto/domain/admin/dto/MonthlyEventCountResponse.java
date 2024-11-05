package com.runto.domain.admin.dto;

import lombok.Getter;

@Getter
public class MonthlyEventCountResponse {

    private String month;
    private Long count;

    public MonthlyEventCountResponse(String month, Long count) {
        this.month = month;
        this.count = count;
    }
}
