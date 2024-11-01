package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyDetailsResponse {

    private String nickname;

    private String email;

    @JsonProperty("penalty")
    private Long penalty;

}
