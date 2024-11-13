package com.runto.domain.chat.config;

import com.runto.domain.chat.dto.CustomPrincipal;
import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.dto.UserDetailsDTO;
import com.runto.global.security.util.JWTUtil;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null){
                Map<String, Object> attributes = accessor.getSessionAttributes();
                authHeader = (String) attributes.get("Authorization");
                log.info("Jwt Channel Interceptor authHeader = {}",authHeader);

                Authentication authentication = (Authentication) accessor.getUser();
                log.info("Jwt Channel Interceptor authentication = {}",authentication);
                accessor.setUser(authentication);
            }

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Long userId = jwtUtil.getId(token);
                    String username = jwtUtil.getUsername(token);
                    String role = jwtUtil.getRole(token);
                    String status = jwtUtil.getStatus(token);

                    CustomUserDetails userDetails = new CustomUserDetails(new UserDetailsDTO(userId, null, null, null, username, null,status, role));
                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(new SimpleGrantedAuthority(role)));

                    accessor.setUser(authenticationToken);

                } catch (Exception e) {
                    throw new RuntimeException("Invalid token: " + e.getMessage());
                }
            }
        }

        return message;
    }
}
