package com.runto.domain.gathering.api;

import com.runto.domain.gathering.application.GatheringService;
import com.runto.domain.gathering.dto.CreateGatheringRequest;
import com.runto.domain.gathering.dto.GatheringDetailResponse;
import com.runto.domain.gathering.dto.UserGatheringsRequestParams;
import com.runto.domain.gathering.dto.UserGatheringsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gatherings")
public class GatheringController {

    private final GatheringService gatheringService;

    // TODO: 회원관련 기능 dev에 머지되면 param 에 UserDetails 추가 & 교체
    @PostMapping
    public ResponseEntity<Void> createGathering(@Valid @RequestBody CreateGatheringRequest request) {

        gatheringService.createGatheringGeneral(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gathering_id}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(
            @PathVariable("gathering_id") Long gatheringId) {
        return ResponseEntity.ok(gatheringService.getGatheringDetail(gatheringId));
    }


    // TODO userId -> userDetails 로 바꿔야함
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
