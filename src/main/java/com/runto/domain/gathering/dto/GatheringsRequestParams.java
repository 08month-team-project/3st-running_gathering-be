package com.runto.domain.gathering.dto;

import com.runto.domain.gathering.type.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

// 참고: @ModelAttribute는 요청 데이터 바인딩 과정에서 ObjectMapper를 사용하지 않음 -> setter 필요, @JsonNaming 미작동
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatheringsRequestParams { // Query Parameter 로 매핑 (ModelAttribute 적용)


    @NotNull(message = "gathering_type 은 필수 값입니다.")
    private GatheringType gatheringType;

    private String searchTitle;     // 키워드 검색은 es를 적용하기로 했으나, 못하게 될수도 있으니 일단 rdbms의 like 문으로 처리

    private GoalDistance goalDistance;     // null 이면 전체조회

    private RunningConcept runningConcept;     // null 이면 전체조회

    private ParticipationEligibility participationEligibility; // 참가 가능여부 (null 일 경우 전체조회)

    @NotNull(message = "order_by 는 필수 값입니다.")
    private GatheringOrderField orderBy;

    @NotNull(message = "sort_direction 은 필수 값입니다.")
    private Sort.Direction sortDirection;

    // CoordinatesDto 자체는 안 받아도 되는데, 받게되면 안의 값이 모두 있어야함
    private GeoRadiusDto geoRadius;


    // TODO: ModelAttribute를  적용하는 곳이 많아지면 별도의 필터를 구현할 것을 고려
    // snake case를 처리할 수 있는 별도의 Setter 선언
    public void setGathering_type(GatheringType gatheringType) {
        this.gatheringType = gatheringType;
    }

    public void setSearch_title(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public void setGoal_distance(GoalDistance goalDistance) {
        this.goalDistance = goalDistance;
    }

    public void setRunning_concept(RunningConcept runningConcept) {
        this.runningConcept = runningConcept;
    }

    public void setParticipation_eligibility(ParticipationEligibility participationEligibility) {
        this.participationEligibility = participationEligibility;
    }

    public void setOrder_by(@NotNull GatheringOrderField orderBy) {
        this.orderBy = orderBy;
    }

    public void setSort_direction(@NotNull Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setGeo_radius(GeoRadiusDto geoRadius) {
        this.geoRadius = geoRadius;
    }
}
