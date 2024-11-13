package com.runto.domain.chat.config;

import com.runto.domain.chat.dto.CustomPrincipal;
import com.runto.global.security.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
@Slf4j
@Component
public class WebSocketHandShakeHandler extends DefaultHandshakeHandler {

    private final JWTUtil jwtUtil;

    public WebSocketHandShakeHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Authorization");
        if (token == null){
            log.info("WebSocketHandShakeHandler : Authorization 에 토큰 없음 attribute 에서 받기");
            token =  (String) attributes.get("Authorization");
            log.info("WebSocketHandShakeHandler :  attribute 에서 받은 토큰 = {}",token);
        }
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return null;
        }
        // JWT 토큰을 검증하고 사용자 정보 추출
        if (!jwtUtil.isExpired(token)) {
            String username = jwtUtil.getUsername(token);
            Long id = jwtUtil.getId(token);
            return new CustomPrincipal(id,username);
        } else {
            return null;
        }
    }
}
