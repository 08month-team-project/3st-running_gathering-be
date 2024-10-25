package com.runto.domain.gathering.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.runto.domain.gathering.domain.Gathering;
import com.runto.domain.gathering.type.GoalDistance;
import com.runto.domain.gathering.type.RunningConcept;
import com.runto.domain.image.dto.GatheringImageUrlsDto;
import com.runto.domain.user.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

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

    @Range(min = 2, max = 10, message = "모임 최대인원은 2 ~ 10명 이어야합니다.")
    private int maxNumber;

    @NotBlank(message = "본문 내용은 필수값입니다.")
    @Length(min = 10, max = 200, message = "본문 내용은 10~200 자 내로 입력해주세요.")
    private String description;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private GoalDistance goalDistance;

    @NotNull(message = "목표 km 를 설정해야합니다.")
    private RunningConcept concept;

    @Valid
    private GatheringImageUrlsDto gatheringImageUrls;

    public Gathering toEntity(User host) {

        return Gathering.builder()
                .host(host)
                .title(title)
                .description(description)
                .appointedAt(appointedAt)
                .deadline(deadline)
                .concept(concept)
                .goalDistance(goalDistance)
                .thumbnailUrl(gatheringImageUrls.getRepresentativeImageUrl())
                .location(location.toLocation())
                .maxNumber(maxNumber)
                .build();
    }
}
