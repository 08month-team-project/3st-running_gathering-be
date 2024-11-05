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

    private String title;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("address_name")
    private String addressName;

    private EventRequestStatus status;

    @JsonProperty("report_reason")
    private ReportReason reportReason;

    private String email;

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
