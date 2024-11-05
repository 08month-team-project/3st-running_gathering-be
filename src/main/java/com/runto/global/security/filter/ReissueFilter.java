package com.runto.global.security.filter;

import com.runto.domain.user.dao.RefreshRepository;
import com.runto.global.security.util.JWTUtil;
import com.runto.global.security.util.RefreshUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ReissueFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final RefreshUtil refreshUtil;
    private final RefreshRepository refreshRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (!requestUri.matches("^/users/reissue$")
                ||(!requestMethod.equals("POST"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        //만료 체크
        try{
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //이 토큰 refresh?
        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //DB에 저장되어있는지?
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            //response body
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long userId = jwtUtil.getId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //다맞다면 엑세스 재발급
        String newAccess = jwtUtil.createJwt("access",userId,username,role,2*60*60*1000L);
        //Refresh Rotate
        String newRefresh = jwtUtil.createJwt("refresh",userId,username,role,3*24*60*60*1000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        refreshUtil.addRefreshEntity(username, newRefresh, 3*24*60*60*1000L);

        response.setHeader("Authorization", "Bearer "+newAccess);
        ResponseCookie cookie = refreshUtil.createCookie("refresh", newRefresh);
        response.addHeader("Set-Cookie", cookie.toString());
    /*
    Rotate 되기 이전의 토큰을 가지고 서버측으로 가도 인증이 되기 때문에 서버측에서 발급했던
     Refresh들을 기억한 뒤 블랙리스트 처리를 진행하는 로직을 작성해야 한다.
     */
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
