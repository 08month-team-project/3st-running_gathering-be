package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttendanceEventGatheringRequest { // 이벤트모임은 한번에 별개의 값으로 처리 불가하게 하였음

    @NotNull(message = "real_distance 은 필수값입니다.")
    private Double realDistance;

    @NotNull(message = "member_id_list 는 필수값입니다.")
    private List<Long> memberIdList;
}
