package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GatheringTimeStatus { // 일반목록조회, 내 모임 조회 둘 다 사용할 예정

    UPCOMING("신청기한이 지나지 않은 모임"),
    ENDED("종료된 모임"),
    ONGOING("종료되지 않은 모임");

    private final String description;

}
