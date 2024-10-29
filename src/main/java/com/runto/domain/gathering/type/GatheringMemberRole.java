package com.runto.domain.gathering.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GatheringMemberRole {

    ORGANIZER("주최자"),
    PARTICIPANT("참여자");

    private final String description;
}
