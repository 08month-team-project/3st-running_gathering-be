package com.runto.domain.chat.api;

import com.runto.domain.chat.application.ProducerService;
import com.runto.domain.chat.dto.MessageDTO;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;

    //1:1 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/direct")
    public void sendDirectMessage(@Payload MessageDTO messageDTO, StompHeaderAccessor headerAccessor){
        Authentication authentication = (Authentication) headerAccessor.getUser();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now().plusHours(9));
        }
        producerService.sendDirectMessage(messageDTO,userId);
    }

    //그룹 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/group")
    public void sendGroupMessage(@Payload MessageDTO messageDTO,StompHeaderAccessor headerAccessor){
        Authentication authentication = (Authentication) headerAccessor.getUser();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now().plusHours(9));
        }
        producerService.sendGroupMessage(messageDTO, userId);
    }

}
