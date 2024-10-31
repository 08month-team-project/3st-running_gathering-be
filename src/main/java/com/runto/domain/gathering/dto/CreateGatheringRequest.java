package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.GatheringType;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import com.runto.domain.image.dto.ImageRegisterResponse;
import com.runto.domain.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateGatheringRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 5, max = 20, message = "제목은 5~20자 내로 입력해주세요.")
    private String title;

    @NotNull(message = "약속날짜시간은 필수값입니다.")
    private LocalDateTime appointedAt; // 약속날짜시각

    @NotNull(message = "지원마감날짜는 필수값입니다.")
    private LocalDateTime deadline; // 모집마감 날짜시각

    @Valid
    @NotNull(message = "약속장소는 필수값입니다.")
    private LocationDto location;

    //@ValidGatheringMaxNumber // TODO 에러해결되면 교체 (현재는 서비스에서 따로 검증 후 예외발생시키는걸로 임시조치)
    private int maxNumber;

    @NotBlank(message = "본문 내용은 필수값입니다.")
    @Length(min = 10, max = 200, message = "본문 내용은 10~200 자 내로 입력해주세요.")
    private String description;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private GoalDistance goalDistance;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private RunningConcept concept;

    @NotNull(message = "모임 타입은 필수값입니다.")
    private GatheringType gatheringType;

    @Valid
    private ImageRegisterResponse imageRegisterResponse;


    public Gathering toEntity(User organizer) {

        return Gathering.builder()
                .organizerId(organizer.getId())
                .title(title)
                .description(description)
                .appointedAt(appointedAt)
                .deadline(deadline)
                .concept(concept)
                .goalDistance(goalDistance)
                .thumbnailUrl(imageRegisterResponse.getRepresentativeImageUrl())
                .location(location.toLocation())
                .maxNumber(maxNumber)
                .gatheringType(gatheringType)
                .build();
    }
}
