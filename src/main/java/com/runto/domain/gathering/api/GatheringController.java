package com.runto.domain.gathering.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.CreateGatheringRequest;
import com.runto.domain.gathering.dto.GatheringDetailResponse;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.dto.UserGatheringsResponse;
import com.runto.global.security.detail.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gatherings")
public class GatheringController {

    private final GatheringService gatheringService;


    @PostMapping
    public ResponseEntity<Void> createGathering(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        gatheringService.createGatheringGeneral(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/event")
    public ResponseEntity<Void> requestEventGatheringHosting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        log.info("userId = {}", userDetails.getUserId());
        gatheringService.requestEventGatheringHosting(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    // TODO: 상세조회 시엔 DELETED, REPORTED는 노출 X
    @GetMapping("/{gathering_id}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
            @PathVariable("gathering_id") Long gatheringId) {
        return ResponseEntity.ok(gatheringService.getGatheringDetail(gatheringId));
    }


    // TODO: 목록 조회시엔 DELETED 외에는 모두 노출 (대신 상세보기는 막는걸로)
    // TODO: userId -> userDetails 로 바꿔야함
    // TODO: Gathering 에 type 필드 추가로 인해, 필터링 값 추가, 쿼리문 조건 추가 필요
    @GetMapping
    public ResponseEntity<UserGatheringsResponse> getMyGatherings(
            @RequestParam(name = "user_id") Long userId, // before 유저인증 적용
            @PageableDefault(size = 8) Pageable pageable,
            @Valid @ModelAttribute UserGatheringsRequestParams requestParams
    ) {

        return ResponseEntity.ok(gatheringService
                .getUserGatherings(userId, pageable, requestParams));
    }

}
