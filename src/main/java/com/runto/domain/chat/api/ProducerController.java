package com.runto.domain.chat.api;

import com.runto.domain.chat.application.ProducerService;
import com.runto.domain.chat.dto.MessageDTO;
import com.runto.global.security.detail.CustomUserDetailService;
import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.security.Principal;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;
    private final JWTUtil jwtUtil;

    //1:1 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/direct")
    public void sendDirectMessage(@Payload MessageDTO messageDTO, StompHeaderAccessor headerAccessor){
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        Long id = jwtUtil.getId(token);

//        SecurityContext context = SecurityContextHolder.getContext();
//        log.info("ProducerController direct context = {}",context);
//        Authentication authentication = context.getAuthentication();
//        log.info("ProducerController direct authentication = {}",authentication);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        log.info("ProducerController direct userDetails userId = {}",userDetails.getUserId());
        if (messageDTO.getTimestamp() == null){
            messageDTO.setTimestamp(LocalDateTime.now());
        }
//        producerService.sendDirectMessage(messageDTO, userDetails.getUserId());
        producerService.sendDirectMessage(messageDTO,id);
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

//    //그룹 채팅 웹소켓 메시지 전송
//    @MessageMapping("/send/group")
//    public void sendGroupMessage(@Payload MessageDTO messageDTO,
//                                  @AuthenticationPrincipal CustomUserDetails userDetails){
//        log.info("ProducerController userDetails userId = {}",userDetails.getUserId());
//        if (messageDTO.getTimestamp() == null){
//            messageDTO.setTimestamp(LocalDateTime.now());
//        }
//        producerService.sendGroupMessage(messageDTO, userDetails.getUserId());
//    }

    //그룹 채팅 웹소켓 메시지 전송
    @MessageMapping("/send/group")
    public void sendGroupMessage(@Payload MessageDTO messageDTO){
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("ProducerController group context = {}",context);
        Authentication authentication = context.getAuthentication();
        log.info("ProducerController group authentication = {}",authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("ProducerController group userDetails userId = {}",userDetails.getUserId());
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
