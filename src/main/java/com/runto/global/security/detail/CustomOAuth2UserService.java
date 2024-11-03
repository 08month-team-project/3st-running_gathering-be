package com.runto.global.security.detail;


import com.runto.domain.user.dao.OAuth2Repository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.OAuth2;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.security.dto.KakaoResponse;
import com.runto.global.security.dto.OAuth2Response;
import com.runto.global.security.dto.OAuth2DetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.runto.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuth2Repository OAuth2Repository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        OAuth2Response oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String oAuth2Key = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String username = oAuth2Response.getName();
        String userEmail = oAuth2Response.getEmail();

        Optional<OAuth2> existData = OAuth2Repository.findByOAuth2Key(oAuth2Key);

        //회원가입 시켜버리기
        if (existData.isEmpty()) {
            User userEntity = User.of(userEmail,username)
                    .withOAuth2(oAuth2Key);

            userRepository.save(userEntity);

            return new CustomOAuth2User(OAuth2DetailsDTO.of(userEntity));
        }else {
            OAuth2Repository.save(existData.get());
            User user = userRepository.findBySocialUsername(oAuth2Key)
                    .orElseThrow(()->new UserException(USER_NOT_FOUND));

            return new CustomOAuth2User(OAuth2DetailsDTO.of(user));
        }
    }
}
