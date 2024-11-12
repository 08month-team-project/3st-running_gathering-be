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
import org.springframework.messaging.simp.stomp.StompCommand;
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
                .addInterceptors(customHttpSessionHandshakeInterceptor())
                .withSockJS()
//                .setClientLibraryUrl("https://cdn.jsdelivr.net/sockjs/1.6.1/sockjs.min.js")
        ;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                        .enableStompBrokerRelay("/topic");
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
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
        registration.interceptors(jwtChannelInterceptor());
//        // SecurityContextHolder의 전략을 InheritableTHREADLOCAL로 설정하여 WebSocket 스레드에서 상속 가능하도록 한다.
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
//                    String authHeader = headerAccessor.getFirstNativeHeader("Authorization");
//                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                        String token = authHeader.substring(7);
//                        try {
//                            Long userId = jwtUtil.getId(token);
//                            String username = jwtUtil.getUsername(token);
//                            String role = jwtUtil.getRole(token);
//
//                            CustomUserDetails userDetails = new CustomUserDetails(new UserDetailsDTO(userId, null, null, null, username, null, role));
//                            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(role)));
//
//                            // 새로운 SecurityContext 생성 및 설정
//                            SecurityContext context = SecurityContextHolder.createEmptyContext();
//                            context.setAuthentication(authenticationToken);
//                            SecurityContextHolder.setContext(context);
//
//                            log.info("새로운 WebSocket 연결: SecurityContext 설정 완료 (UserID: {}, Username: {})", userId, username);
//                        } catch (Exception e) {
//                            throw new RuntimeException("Invalid token: " + e.getMessage());
//                        }
//                    }
//                }
//                return message;
//            }
//
//            @Override
//            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//                StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//                if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
//                    // WebSocket 연결 종료 후 SecurityContext 초기화
//                    SecurityContextHolder.clearContext();
//                    log.info("WebSocket 연결 종료 후 SecurityContext 초기화 완료");
//                }
//            }
//        });
    }

    @Bean
    public JwtChannelInterceptor jwtChannelInterceptor(){
        return new JwtChannelInterceptor(jwtUtil);
    }

    @Bean
    public CustomHttpSessionHandShakeInterceptor customHttpSessionHandshakeInterceptor() {
        return new CustomHttpSessionHandShakeInterceptor();
    }
}




