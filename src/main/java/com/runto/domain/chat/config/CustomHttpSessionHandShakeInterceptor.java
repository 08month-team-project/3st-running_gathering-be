package com.runto.domain.chat.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Slf4j
public class CustomHttpSessionHandShakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest servletRequest){
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies == null) {
                return true;
            }
            for (Cookie cookie : cookies){
                if ("Authorization".equals(cookie.getName())){
                    log.info("CustomHttpSessionHandShakeInterceptor Cookie Authorization = {}",cookie.getValue());
                    attributes.put("Authorization","Bearer "+cookie.getValue());
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
    }

    private HttpSession getSession(ServerHttpRequest request) {
        return ((ServletServerHttpRequest)request).getServletRequest().getSession();
    }
}
