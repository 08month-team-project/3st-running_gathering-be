package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GatheringOrderField { // 일반목록조회, 내 모임 조회 둘 다 사용할 예정

    APPOINTED_AT("모임약속날", "appointedAt"),
    DEADLINE("신청 마감", "deadline"),
    HITS("조회수","hits"),
    CREATED_AT("생성일", "createdAt");

    private final String description;
    private final String name;

}
