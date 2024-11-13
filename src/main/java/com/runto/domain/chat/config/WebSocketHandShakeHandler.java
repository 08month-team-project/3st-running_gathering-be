package com.runto.domain.chat.config;

import com.runto.global.security.util.JWTUtil;
import com.sun.security.auth.UserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

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

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // "Bearer " 부분을 제거
        } else {
            return null;  // 토큰이 없는 경우 null을 반환하여 연결을 거부
        }

        // JWT 토큰을 검증하고 사용자 정보 추출
        if (!jwtUtil.isExpired(token)) {
            String username = jwtUtil.getUsername(token);
            return new UserPrincipal(username);  // 검증된 사용자의 Principal 반환
        } else {
            return null;  // 토큰이 유효하지 않은 경우 null 반환
        }
    }
}
