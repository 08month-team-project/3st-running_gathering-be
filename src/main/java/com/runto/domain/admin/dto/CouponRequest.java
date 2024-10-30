package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponRequest {


    @JsonProperty("coupon_name")
    @NotBlank(message = "코드 이름은 필수입니다.")
    private String couponName;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private Integer quantity;

    @Future(message = "만료일은 현재 시점 이후여야 합니다.")
    @JsonProperty("expired_at")
    private LocalDateTime expiredAt;

}
