package com.runto.domain.gathering.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.*;
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

        gatheringService.createGatheringGeneral(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/events")
    public ResponseEntity<Void> requestEventGatheringHosting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        log.info("userId = {}", userDetails.getUserId());
        gatheringService.requestEventGatheringHosting(userDetails.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    // TODO: 상세조회 시엔 DELETED, REPORTED는 노출 X
    @GetMapping("/{gathering_id}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
            @PathVariable("gathering_id") Long gatheringId) {
        return ResponseEntity.ok(gatheringService.getGatheringDetail(gatheringId));
    }


    @GetMapping
    public ResponseEntity<UserGatheringsResponse> getMyGatherings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 8) Pageable pageable,
            @Valid @ModelAttribute UserGatheringsRequestParams requestParams) {

        return ResponseEntity.ok(gatheringService
                .getUserGatherings(userDetails.getUserId(), pageable, requestParams));
    }

}
