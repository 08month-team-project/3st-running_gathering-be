package com.runto.domain.chat.config;

import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.dto.UserDetailsDTO;
import com.runto.global.security.util.JWTUtil;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("command = {}",accessor.getCommand());
            log.info("token = {}",accessor.getFirstNativeHeader("Authorization"));
        }
            String authHeader = accessor.getFirstNativeHeader("Authorization");
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

                    log.info("새로운 WebSocket 연결: SecurityContext 설정 완료 (UserID: {}, Username: {})", userId, username);
                } catch (Exception e) {
                    throw new RuntimeException("Invalid token: " + e.getMessage());
                }
            }
        return message;
    }
}
