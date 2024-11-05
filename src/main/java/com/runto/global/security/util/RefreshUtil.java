package com.runto.global.security.util;

import com.runto.domain.user.dao.RefreshRepository;
import com.runto.domain.user.domain.Refresh;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class RefreshUtil {
    private final RefreshRepository refreshRepository;

    public void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();
        refreshRepository.save(refreshEntity);
    }
    public ResponseCookie createCookie(String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key,value)
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .domain("myspringserver.store") // 예시입니다! 서버의 도메인만 적어주면 됨
                .secure(true) // sameSite를 None으로 지정했다면 필수
                .build();

        return cookie;
    }
}
