package com.runto.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runto.domain.user.dao.RefreshRepository;
import com.runto.domain.user.domain.Refresh;
import com.runto.domain.user.dto.LoginRequest;
import com.runto.global.security.detail.CustomUserDetails;
import com.runto.global.security.util.JWTUtil;
import com.runto.global.security.util.RefreshUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshUtil refreshUtil;

    public LoginFilter(JWTUtil jwtUtil, AuthenticationManager authenticationManager, RefreshUtil refreshUtil) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.refreshUtil = refreshUtil;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청에서 username, password 추출

        try {
            if (!request.getMethod().equals("POST")){
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"요청 메소드가 올바르지 않습니다.\"}");
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            if (email.isEmpty() && password.isEmpty()) {
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\":\"이메일과 비밀번호를 입력해 주세요.\"}");
                return null;
            }
//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email,password,null);
            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            response.setContentType("application/json; charset=UTF-8");
            try {
                response.getWriter().write("{\"message\":\"사용자 인증에 실패했습니다.\"}");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            response.setStatus(401);
            return null;
        }
    }
    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String nickname = userDetails.getNickname();
        Long userId = userDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String status = userDetails.getStatus();

        String access = jwtUtil.createJwt("access",userId,username,role,status,2*60*60*1000L);
        String refresh = jwtUtil.createJwt("refresh",userId,username,role,status,3*24*60*60*1000L);

        //Refresh 토큰 저장
        refreshUtil.addRefreshEntity(username, refresh, 3*24*60*60*1000L);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{" +
                "\"nickname\":\""+ nickname+"\"" +
                "}");
        response.addHeader("Authorization","Bearer "+access);

        ResponseCookie cookie = refreshUtil.createCookie("refresh", refresh);
        response.addHeader("Set-Cookie", cookie.toString());
        response.setStatus(HttpStatus.OK.value());
    }
    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
