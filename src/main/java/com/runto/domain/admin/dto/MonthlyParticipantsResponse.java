package com.runto.domain.admin.dto;

import lombok.Getter;

@Getter
public class MonthlyParticipantsResponse {

    private String month;
    private String title;
    private Integer count;

    public MonthlyParticipantsResponse(String month, String title, Integer count) {
        this.month = month;
        this.title = title;
        this.count = count;
    }
}
