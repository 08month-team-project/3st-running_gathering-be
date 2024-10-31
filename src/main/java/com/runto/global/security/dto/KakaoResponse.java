package com.runto.global.security.dto;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attribute;


    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        log.info(attribute.toString());
    }


    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attribute.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");
        if (account != null && account.get("email") != null) {
            return String.valueOf(account.get("email"));
        }
        return "Unknown"; // nickname이 없을 경우 기본 값
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        if (properties != null && properties.get("nickname") != null) {
            return String.valueOf(properties.get("nickname"));
        }
        return "Unknown"; // nickname이 없을 경우 기본 값
    }

}
