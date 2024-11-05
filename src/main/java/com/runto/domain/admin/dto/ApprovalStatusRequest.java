package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runto.domain.gathering.type.EventRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApprovalStatusRequest {

    @NotNull(message = "이벤트 승인 또는 거절은 반드시 입력해주세요!")
    private EventRequestStatus status;

    @NotNull(message = "이메일은 필수입니다.")
    private String email;

    @JsonProperty("report_reason")
    private String reportReason;
}
