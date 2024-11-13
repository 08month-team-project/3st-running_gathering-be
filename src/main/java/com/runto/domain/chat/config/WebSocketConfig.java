package com.runto.domain.chat.config;

import com.runto.global.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTUtil jwtUtil;
    private final WebSocketHandShakeHandler webSocketHandShakeHandler;

    @Value("${servername}")
    private String serverName;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000", serverName,"https://runto.vercel.app/")
                .addInterceptors(customHttpSessionHandshakeInterceptor())
                .setHandshakeHandler(webSocketHandShakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
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




