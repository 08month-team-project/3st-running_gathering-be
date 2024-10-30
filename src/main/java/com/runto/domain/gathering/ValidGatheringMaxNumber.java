package com.runto.domain.gathering;

import com.runto.domain.gathering.type.GatheringType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER }) // 필드, 파라미터에 선언 가능
@Retention(RetentionPolicy.RUNTIME) // 해당 애노테이션이 유지되는 시간 (RUNTIME 까지 유효)
@Constraint(validatedBy = GatheringMaxNumberValidator.class) // 해당 클래스를 통해 유효성 검사 진행
public @interface ValidGatheringMaxNumber {

    // 유효하지 않을 경우 반환되는 메세지
    String message() default "모임 최대인원은 설정된 범위를 초과할 수 없습니다.";

    // 유효성 검증이 진행될 그룹
    Class<?>[] groups() default {};

    // 유효성 검증 시에 전달할 메타 정보
    Class<? extends Payload>[] payload() default {};

}
