package com.runto.domain.admin.dto;

import lombok.Getter;

@Getter
public class MonthlyParticipantsResponse {

    String month;
    String title;
    Integer count;

    public MonthlyParticipantsResponse(String month, String title, Integer count) {
        this.month = month;
        this.title = title;
        this.count = count;
    }
}
