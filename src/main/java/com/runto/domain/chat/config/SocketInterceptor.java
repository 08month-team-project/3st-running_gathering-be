package com.runto.domain.chat.config;

import com.runto.global.security.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SocketInterceptor implements HandshakeInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();

        String token = Optional.ofNullable(jwtUtil.extractAccessToken(req))
                .orElse(jwtUtil.oauthAccessToken(req));

        attributes.put("Authorization",token);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
