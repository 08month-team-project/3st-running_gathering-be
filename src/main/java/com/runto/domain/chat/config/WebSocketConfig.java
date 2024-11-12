package com.runto.domain.chat.config;

import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.dto.UserDetailsDTO;
import com.runto.global.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private final JWTUtil jwtUtil;

    @Value("${servername}")
    private String serverName;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000", serverName)
//                .setAllowedOrigins("https://runto.vercel.app/")
//                .addInterceptors(new SocketInterceptor(jwtUtil))
                .withSockJS()
//                .setClientLibraryUrl("https://cdn.jsdelivr.net/sockjs/1.6.1/sockjs.min.js")
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(5);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500); //첫번째 재시도 대기 시간
        backOffPolicy.setMultiplier(2.0); // 대기 시간 증가 배율
        backOffPolicy.setMaxInterval(10000);// 최대 대기 시간
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                String authHeader = headerAccessor.getFirstNativeHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    try {
                        Long userId = jwtUtil.getId(token);  // JWT에서 사용자 정보 추출
                        log.info("InboundChannel userId = {}",userId);
                        String username = jwtUtil.getUsername(token);
                        log.info("InboundChannel username = {}",username);
                        String role = jwtUtil.getRole(token);

                        // 인증된 사용자 정보 설정
                        CustomUserDetails userDetails = new CustomUserDetails(new UserDetailsDTO(userId,null,null,null,username,null,role));
                        log.info("InboundChannel userDetails userId = {}",userDetails.getUserId());
                        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(role)));
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authenticationToken);
                        SecurityContextHolder.setContext(context);  // WebSocket 스레드에만 유효한 context 설정

                        log.info("InboundChannel Context Authentication = {}", SecurityContextHolder.getContext().getAuthentication());
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid token: " + e.getMessage());
                    }
                }
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                SecurityContextHolder.clearContext();
            }
        });
    }


//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                String authHeader = String.valueOf(accessor.getNativeHeader("Authorization"));
//                log.info("authHeader = {}",authHeader);
//
//
//                StompCommand command = accessor.getCommand();
//
//                if (command.equals(StompCommand.UNSUBSCRIBE) || command.equals(StompCommand.MESSAGE) ||
//                command.equals(StompCommand.CONNECTED) || command.equals(StompCommand.SEND)){
//                    return message;
//                }else if (command.equals(StompCommand.ERROR)){
//                    log.error("STOMP ERROR");
//                    throw new RuntimeException("STOMP 에러");
//                }
//
//                if (authHeader == null){
//                    log.error("Authorization Header 가 존재하지 않습니다");
//                    throw new RuntimeException("헤더가 존재하지 않음");
//                }
//
//                List<String> authHeaderList = accessor.getNativeHeader("Authorization");
//                log.info("authHeaderList = {}",authHeaderList);
//
//                if (authHeaderList == null || authHeaderList.isEmpty()) {
//                    log.error("Authorization 헤더가 존재하지 않습니다");
//                    throw new RuntimeException("헤더가 존재하지 않음");
//                }
//                String token = "";
//                String authHeaderStr = authHeaderList.get(0).trim(); // 첫 번째 헤더 값을 추출하고, 앞뒤 공백을 제거
//
//                if (authHeaderStr.startsWith("Bearer ")) {
//                    token = authHeaderStr.substring(7);  // "Bearer " 이후 부분만 추출
//                } else {
//                    log.error("Authorization 헤더 형식이 틀립니다: {}", authHeaderStr);
//                    throw new RuntimeException("올바르지 않은 헤더 형식");
//                }
//
//
//                try {
//                    Long userId = jwtUtil.getId(token);
//                    String username = jwtUtil.getUsername(token);
//                    String role = jwtUtil.getRole(token);
//
//                    CustomUserDetails userDetails = new CustomUserDetails(new UserDetailsDTO(userId,username,null,null,null,null,role));
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(role)));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                } catch (Exception e) {
//                    log.error("JWT 처리 중 오류 발생: {}", e.getMessage());
//                    throw new RuntimeException("유효하지 않은 토큰입니다.");
//                }
//
//                return message;
//            }
//        });
//    }

}




