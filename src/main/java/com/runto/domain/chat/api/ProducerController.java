package com.runto.domain.chat.api;

import com.runto.domain.chat.application.ProducerService;
import com.runto.domain.chat.dto.MessageDTO;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;

    //1:1 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/direct")
    public void sendDirectMessage(@Payload MessageDTO messageDTO,
                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now());
        }
        producerService.sendDirectMessage(messageDTO, userDetails.getUserId());
    }

    //1:1 채팅 테스트 메시지 전송 api
    @PostMapping("/send/message/direct")
    public void sendMessageDirect(@RequestBody MessageDTO messageDTO,
                            @AuthenticationPrincipal CustomUserDetails userDetails){
        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now());
        }
        producerService.sendDirectMessage(messageDTO, userDetails.getUserId());
    }

    //그룹 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/group")
    public void sendGroupMessage(@Payload MessageDTO messageDTO,
                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now());
        }
        producerService.sendGroupMessage(messageDTO, userDetails.getUserId());
    }

    //그룹 채팅 테스트 메시지 전송 api
    @PostMapping("/send/message/group")
    public void sendMessageGroup(@RequestBody MessageDTO messageDTO,
                            @AuthenticationPrincipal CustomUserDetails userDetails){
        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now());
        }
        producerService.sendGroupMessage(messageDTO, userDetails.getUserId());
    }
}
