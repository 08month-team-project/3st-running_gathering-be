package com.runto.domain.chat.api;

import com.runto.domain.chat.application.DirectChatService;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chat/direct")
@RequiredArgsConstructor
public class DirectChatController {
    private final DirectChatService directChatService;

    //1:1 채팅방 생성 api
    @PostMapping
    public ResponseEntity<Void> createDirectChatRoom(@RequestParam(name = "other_id") Long otherId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        directChatService.createDirectChat(userDetails.getUsername(), otherId);
        return ResponseEntity.ok().build();
    }

}
