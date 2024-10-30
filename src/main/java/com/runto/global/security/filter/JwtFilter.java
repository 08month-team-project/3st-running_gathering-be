package com.runto.global.security.filter;

import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.dto.UserDetailsDTO;
import com.runto.global.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.extractAccessToken(request);

        if(accessToken == null) {
            System.out.println("Authorization header is missing");
            filterChain.doFilter(request, response);
            return;
        }
        //토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e){
            //response body
            PrintWriter writer = response.getWriter();
            writer.println("AccessToken expired");
            //상태코드
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //토큰이 access인지 확인(발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if(!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("Not an access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String username = jwtUtil.getUsername(accessToken);
        Long userId= jwtUtil.getId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserDetailsDTO user = UserDetailsDTO.builder()
                .userId(userId)
                .email(username)
                .role(role)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
