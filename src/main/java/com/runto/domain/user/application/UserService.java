package com.runto.domain.user.application;

import com.runto.domain.user.dao.LocalAccountRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.dto.SignupResponse;
import com.runto.domain.user.excepction.UserException;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import com.runto.domain.user.type.UserStatus;
import com.runto.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LocalAccountRepository localAccountRepository;

    @Transactional
    public SignupResponse createUser(@Valid SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user->{throw new UserException(ErrorCode.ALREADY_EXIST_USER);});

        String encodedPwd = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        //s3 이미지
        User user = User.builder()
                .email(signupRequest.getEmail())
                .nickname(signupRequest.getNickname())
                .gender(Gender.NONE)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        LocalAccount localAccount = LocalAccount.builder()
                .user(user)
                .password(encodedPwd)
                .build();
        localAccountRepository.save(localAccount);

        return SignupResponse.builder().message("가입완료").build();
    }
}
