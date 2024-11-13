package com.runto.global.security.detail;

import com.runto.global.security.dto.OAuth2DetailsDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2DetailsDTO userDTO;

    public CustomOAuth2User(OAuth2DetailsDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userDTO.getNickname();

    }

    public String getEmail() {
        return userDTO.getEmail();
    }

    public String getStatus(){
        return userDTO.getStatus();
    }

    public Long getId(){
        return userDTO.getUserId();
    }
}
