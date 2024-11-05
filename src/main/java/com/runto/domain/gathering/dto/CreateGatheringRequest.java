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

// 등록 api 를 분리하면서 요청값에서 GatheringType 값을 삭제하였음
// request 의 type 값을 받아서 Gathering 을 생성하던 상황 -> 일반모임생성인데 이벤트를 요청값에 넣어서 이벤트타입의 모임글로 생성될 수 있었음
// 예외처리보다는 type 값 자체를 받지 않는 방식을 택하였음

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

    private int maxNumber;

    @NotBlank(message = "본문 내용은 필수값입니다.")
    @Length(min = 10, max = 200, message = "본문 내용은 10~200 자 내로 입력해주세요.")
    private String description;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private GoalDistance goalDistance;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private RunningConcept concept;


    @Valid
    private ImageRegisterResponse imageRegisterResponse; // 이미지 등록 자체를 안한다면 null


    public Gathering toEntity(User organizer, GatheringType type) {

        String thumbnailUrl =
                (imageRegisterResponse == null) ?
                        null : imageRegisterResponse.getRepresentativeImageUrl();


        return Gathering.builder()
                .organizerId(organizer.getId())
                .title(title)
                .description(description)
                .appointedAt(appointedAt)
                .deadline(deadline)
                .concept(concept)
                .goalDistance(goalDistance)
                .thumbnailUrl(thumbnailUrl)
                .location(location.toLocation())
                .maxNumber(maxNumber)
                .gatheringType(type)
                .build();
    }
}
