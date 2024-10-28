package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class MonthUserResponse {

    private String month;

    @JsonProperty("user_count")
    private Long userCount;

    public MonthUserResponse(String month, Long userCount) {
        this.month = month;
        this.userCount = userCount;
    }

}
