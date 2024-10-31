package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = Shape.STRING)
@RequiredArgsConstructor
public enum ParticipationEligibility { // 참가 가능 여부

    AVAILABLE("참여 가능"),
    NOT_AVAILABLE("참여 불가");

    private final String description;
}
