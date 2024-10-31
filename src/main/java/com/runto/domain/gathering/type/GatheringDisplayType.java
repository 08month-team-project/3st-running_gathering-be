package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.*;

@JsonFormat(shape = STRING)
@RequiredArgsConstructor
public enum GatheringDisplayType { // 이름이 모호하다..

    ALL("전체", "참여 불가 및 가능한 모임 모두 포함합니다."),

    AVAILABLE("참여 가능", "최대 정원에 미달하고 모집일자가 유효한 경우"),
    CLOSING_SOON("마감 임박", "신청일자가 1일 이내 남았거나 신청 인원이 2명 이하로 남은 경우");


    private final String name;
    private final String description;


}
