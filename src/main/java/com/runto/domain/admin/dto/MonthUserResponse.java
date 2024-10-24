package com.runto.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class MonthUserResponse {

    private String month;
    private Integer activeUserCount;
    private Integer deactivatedUserCount;

    public MonthUserResponse(String month, Integer activeUserCount, Integer deactivatedUserCount) {
        this.month = month;
        this.activeUserCount = activeUserCount;
        this.deactivatedUserCount = deactivatedUserCount;
    }

}
