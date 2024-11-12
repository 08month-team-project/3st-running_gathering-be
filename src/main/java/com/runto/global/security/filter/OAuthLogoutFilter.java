package com.runto.global.security.filter;

import com.runto.global.security.oauth2.KakaoAPI;
import com.runto.global.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuthLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private KakaoAPI kakaoApi;



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (!requestUri.matches("^/users/oauth2/logout$")
                || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = jwtUtil.oauthAccessToken(request);

        if (accessToken != null && !"".equals(accessToken)) {
            kakaoApi = new KakaoAPI();
            kakaoApi.logout(accessToken);

            Cookie cookie = new Cookie("Authorization", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            log.info("logout success");
        }
    }

}
