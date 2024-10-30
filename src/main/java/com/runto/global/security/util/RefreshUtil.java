package com.runto.global.security.util;

import com.runto.domain.user.dao.RefreshRepository;
import com.runto.domain.user.domain.Refresh;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
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
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60*1000);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
