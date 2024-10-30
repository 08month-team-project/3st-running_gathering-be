package com.runto.domain.gathering;

import com.runto.domain.gathering.dto.CreateGatheringRequest;
import com.runto.domain.gathering.type.GatheringType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.runto.domain.gathering.type.GatheringType.*;

public class GatheringMaxNumberValidator implements ConstraintValidator<ValidGatheringMaxNumber, Integer> {
    public static final String GENERAL_MAX_NUMBER_MESSAGE
            = "일반 모임의 최대 인원은 2명에서 10명 사이여야 합니다.";
    public static final String EVENT_MAX_NUMBER_MESSAGE
            = "이벤트 모임의 최대 인원은 10명에서 300명 사이여야 합니다.";


    @Override
    public boolean isValid(Integer maxNumber, ConstraintValidatorContext context) {

        CreateGatheringRequest request = context.unwrap(CreateGatheringRequest.class);
        GatheringType type = request.getGatheringType();

        if (GENERAL.equals(type) && (maxNumber < 2 || maxNumber > 10)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GENERAL_MAX_NUMBER_MESSAGE)
                    .addConstraintViolation();

            return false;
        }
        if (EVENT.equals(type) && (maxNumber < 10 || maxNumber > 300)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(EVENT_MAX_NUMBER_MESSAGE)
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
