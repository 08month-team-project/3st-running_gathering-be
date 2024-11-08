package com.runto.domain.gathering.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.*;
import com.runto.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gatherings")
public class GatheringController {

    private final GatheringService gatheringService;


    @Operation(summary = "일반 모임 등록")
    @PostMapping
    public ResponseEntity<CreateGatheringResponse> createGathering(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGatheringRequest request) {

        return ResponseEntity.ok(gatheringService
                .createGatheringGeneral(userDetails.getUserId(), request));
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

    @Operation(summary = "모임 상세조회 [일반모임, 이벤트모임]") // 같이 쓰게 된 이유는 pr 참조
    @GetMapping("/{gathering_id}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("gathering_id") Long gatheringId) {

        return ResponseEntity.ok(gatheringService.
                getGatheringDetail(userDetails.getUserId(), gatheringId));
    }

    @Operation(summary = "모임목록 조회 [일반모임, 이벤트모임]")
    @GetMapping
    public ResponseEntity<GatheringsResponse> getGatherings(
            @Valid @ModelAttribute GatheringsRequestParams requestParams,
            @PageableDefault(size = 8) Pageable pageable) {

        return ResponseEntity.ok(gatheringService.
                getGatherings(requestParams, pageable));
    }

    @Operation(summary = "지도기반 모임 조회 [일반모임]")
    @GetMapping("/map")
    public ResponseEntity<GatheringsMapResponse> getGeneralGatheringMap(
            @RequestParam("radius_distance") Double radiusDistance,
            @RequestParam("x") BigDecimal x,
            @RequestParam("y") BigDecimal y) {

        return ResponseEntity.ok(gatheringService.
                getGeneralGatheringMap(radiusDistance, x, y));
    }

    @Operation(summary = "모임 구성원목록 조회 [일반모임, 이벤트모임]")
    @GetMapping("/{gathering_id}/members")
    public ResponseEntity<Slice<GatheringMemberResponse>> getGatheringMembers(
            @PathVariable("gathering_id") Long gatheringId,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(gatheringService.
                getGatheringMembers(gatheringId, pageable));
    }

    @Operation(summary = "구성원 출석체크 [일반모임]")
    @PatchMapping("/{gathering_id}/members/attendance")
    public ResponseEntity<List<MemberAttendanceStatusDto>> checkAttendanceGeneralGatheringMembers(
            @PathVariable("gathering_id") Long gatheringId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody List<MemberAttendanceStatusDto> requestList) {

        return ResponseEntity.ok(
                gatheringService.checkAttendanceGeneralGatheringMembers(
                        userDetails.getUserId(), gatheringId, requestList));
    }

    @Operation(summary = "구성원 출석체크 [이벤트 모임]")
    @PatchMapping("/{gathering_id}/event/members/attendance")
    public ResponseEntity<AttendanceEventGatheringResponse> checkAttendanceEventGatheringMembers(
            @PathVariable("gathering_id") Long gatheringId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AttendanceEventGatheringRequest request) {

        return ResponseEntity.ok(gatheringService.
                checkAttendanceEventGatheringMembers(
                        userDetails.getUserId(), gatheringId, request));
    }


    @Operation(summary = " 모임 정상완료 체크 [일반모임, 이벤트모임]")
    @PatchMapping("/{gathering_id}/completion")
    public ResponseEntity<Void> checkCompleteGathering(
            @PathVariable("gathering_id") Long gatheringId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        gatheringService.updateCompleteGathering(userDetails.getUserId(), gatheringId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = " 모임 참가 [일반모임, 이벤트모임]")
    @PostMapping("/{gathering_id}/participation")
    public ResponseEntity<ParticipateGatheringResponse> participateGathering(
            @PathVariable("gathering_id") Long gatheringId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(gatheringService
                .participateGathering(userDetails.getUserId(), gatheringId));
    }

    @Operation(summary = " 모임 참가취소 [일반모임, 이벤트모임]")
    @DeleteMapping("/{gathering_id}/participation")
    public ResponseEntity<Void> cancelParticipateGathering(
            @PathVariable("gathering_id") Long gatheringId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        gatheringService.cancelParticipateGathering(userDetails.getUserId(), gatheringId);
        return ResponseEntity.ok().build();
    }


}
