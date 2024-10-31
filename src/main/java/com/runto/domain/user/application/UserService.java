package com.runto.domain.user.application;

import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.dto.CheckEmailRequest;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.excepction.UserException;
import com.runto.domain.user.type.UserStatus;
import com.runto.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void createUser(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user->{throw new UserException(ErrorCode.ALREADY_EXIST_USER);});

        String encodedPwd = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        String nickname = signupRequest.getNickname();
        String email = signupRequest.getEmail();

        User user = User.of(email,nickname,encodedPwd,null);

        userRepository.save(user);
    }
    
    public void checkEmailDuplicate(CheckEmailRequest checkEmailRequest){
        Optional<User> findUser = userRepository.findByEmail(checkEmailRequest.getEmail());

        if (findUser.isPresent() && findUser.get().getStatus().equals(UserStatus.ACTIVE)){
            throw new UserException(ErrorCode.ALREADY_EXIST_USER);
        }
    }
}
