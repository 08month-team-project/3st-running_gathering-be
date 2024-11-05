package com.runto.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runto.domain.gathering.type.EventRequestStatus;
import com.runto.domain.user.type.ReportReason;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventListResponse {

    @JsonProperty("event_gathering_id")
    private Long eventGatheringId;

    String title;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("address_name")
    String addressName;

    EventRequestStatus status;

    @JsonProperty("report_reason")
    ReportReason reportReason;

    String email;

    public EventListResponse(Long eventGatheringId,String title, LocalDateTime createdAt, String addressName, EventRequestStatus status, ReportReason reportReason, String email) {
        this.eventGatheringId = eventGatheringId;
        this.title = title;
        this.createdAt = createdAt;
        this.addressName = addressName;
        this.status = status;
        this.reportReason = reportReason;
        this.email = email;
    }


}
