package com.runto.domain.gathering.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GatheringLocationType {
    REGION("지역", "지역 코드를 통해 선택된 지역 내 모임을 표시합니다."),
    CURRENT_LOCATION("현재 위치", "현재 위치에서 설정한 반경 내의 모임을 표시합니다.");

    private final String displayName;
    private final String description;

}
