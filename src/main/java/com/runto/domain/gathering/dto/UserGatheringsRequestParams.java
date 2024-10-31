package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.type.GatheringMemberRole;
import com.runto.domain.gathering.type.GatheringOrderField;
import com.runto.domain.gathering.type.GatheringTimeStatus;
import com.runto.domain.gathering.type.GatheringType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

// 참고: @ModelAttribute는 요청 데이터 바인딩 과정에서 ObjectMapper를 사용하지 않음 -> setter 필요, @JsonNaming 미작동
@NoArgsConstructor
@Getter
public class UserGatheringsRequestParams { // Query Parameter 로 매핑 (ModelAttribute 적용)

    private GatheringMemberRole memberRole;

    private GatheringTimeStatus gatheringTimeStatus;

    @NotNull(message = "order_field 는 필수 값입니다.")
    private GatheringOrderField orderBy;

    @NotNull(message = "sort_direction 은 필수 값입니다.")
    private Sort.Direction sortDirection;

    @NotNull(message = "gathering_type 은 필수 값입니다.")
    private GatheringType gatheringType;


    // TODO: ModelAttribute를  적용하는 곳이 많아지면 별도의 필터를 구현할 것을 고려
    // snake case를 처리할 수 있는 별도의 Setter 선언
    public void setMember_role(@NotNull GatheringMemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public void setGathering_time_status(GatheringTimeStatus gatheringTimeStatus) {
        this.gatheringTimeStatus = gatheringTimeStatus;
    }

    public void setOrder_by(@NotNull GatheringOrderField orderBy) {
        this.orderBy = orderBy;
    }

    public void setSort_direction(Sort.@NotNull Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setGathering_type(GatheringType gatheringType) {
        this.gatheringType = gatheringType;
    }
}
