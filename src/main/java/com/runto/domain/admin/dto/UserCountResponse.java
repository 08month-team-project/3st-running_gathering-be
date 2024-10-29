package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCountResponse {

    @JsonProperty("user_count")
    private Long userCount;

    public UserCountResponse(Long userCount) {
        this.userCount = userCount;
    }

}
