package com.runto.domain.gathering.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.*;
import com.runto.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "일반 모임 등록")
    @PostMapping
    public ResponseEntity<Void> createGathering(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        gatheringService.createGatheringGeneral(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이벤트 개최신청 (이벤트 모임 등록)")
    @PostMapping("/events")
    public ResponseEntity<Void> requestEventGatheringHosting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        log.info("userId = {}", userDetails.getUserId());
        gatheringService.requestEventGatheringHosting(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    // TODO: 상세조회 시엔 DELETED, REPORTED는 노출 X
    @Operation(summary = "모임 상세조회 [일반모임,  이벤트모임(아직 미적용)]")
    @GetMapping("/{gathering_id}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
            @PathVariable("gathering_id") Long gatheringId) {
        return ResponseEntity.ok(gatheringService.getGatheringDetail(gatheringId));
    }

    @Operation(summary = "모임목록 조회 [일반모임,  이벤트모임(아직 미적용)]")
    @GetMapping
    public ResponseEntity<GatheringsResponse> getGatherings(
            @Valid @ModelAttribute GatheringsRequestParams requestParams,
            @PageableDefault(size = 8) Pageable pageable) {

        return ResponseEntity.ok(gatheringService.
                getGatherings(requestParams, pageable));
    }
}
