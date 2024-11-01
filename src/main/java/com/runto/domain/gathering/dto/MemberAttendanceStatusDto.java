package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.type.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberAttendanceStatusDto { // 요청값, 반환값

    @NotNull
    private Long memberId;

    @NotNull
    private Long memberAccountId;

    @NotNull
    private AttendanceStatus status;

    @NotNull
    private Double realDistance;


    public static MemberAttendanceStatusDto from(GatheringMember member) {

        return MemberAttendanceStatusDto.builder()
                .memberId(member.getId())
                .memberAccountId(member.getUser().getId())
                .status(member.getAttendanceStatus())
                .realDistance(member.getRealDistance())
                .build();
    }
}
