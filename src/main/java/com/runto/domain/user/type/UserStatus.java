package com.runto.domain.user.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum UserStatus {
    ACTIVE,
    DISABLED,
    BANNED
}
