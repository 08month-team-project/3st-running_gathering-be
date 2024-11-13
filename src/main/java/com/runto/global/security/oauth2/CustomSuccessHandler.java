package com.runto.global.security.oauth2;

import com.runto.global.security.detail.CustomOAuth2User;
import com.runto.global.security.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        Long id = customUserDetails.getId();
        String userEmail = customUserDetails.getEmail();
        String username = customUserDetails.getName();
        String status = customUserDetails.getStatus();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt("access",id,userEmail,role,status,2*60*60*1000L);

        //OAuth2에서는 요청 특성상 응답 헤더로 받을 수 없습니다. 따라서 쿠키 방식으로 받으셔야 합니다.
        response.setContentType("application/json");
        response.getWriter().write("{" +
                "\"username\":\""+ username+"\"" +
                "}");
        ResponseCookie cookie = ResponseCookie.from("Authorization",token)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .domain("myspringserver.store") // 예시입니다! 서버의 도메인만 적어주면 됨
                .secure(true) // sameSite를 None으로 지정했다면 필수
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect("http://localhost:3000");
        //response.sendRedirect("https://runto.vercel.app/");
    }
}