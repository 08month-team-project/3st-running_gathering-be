package com.runto.global.security.detail;

import com.runto.domain.user.dao.LocalAccountRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.exception.ErrorCode;
import com.runto.global.security.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    //loginfilter가 username을 인식못하고 무한루프하게됨.
    // 그래서 이메일을 유저네임으로 바꿔주는 작업
    // 무한루프 방지 코드
    @Transactional
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(UserDetailsDTO.of(user));
    }
}
