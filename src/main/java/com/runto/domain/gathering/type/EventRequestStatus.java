package com.runto.domain.gathering.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventRequestStatus {

    PENDING("대기"),      // 대기
    APPROVED("승인"),     // 승인
    REJECTED("거절");      // 승인거부

    private final String description;
}
