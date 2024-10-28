package com.runto.test_api;

import com.runto.domain.user.dao.LocalAccountRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.runto.domain.user.type.Gender.WOMAN;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserRepository userRepository;
    private final LocalAccountRepository localAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        String password=bCryptPasswordEncoder.encode("123456");

        LocalAccount localAccount = LocalAccount.builder()
                .password(password)
                .build();
        User user = User.builder()
                .email("runto@gmail.com")
                .name("임시유저")
                .nickname("임시유저")
                .gender(WOMAN)
                .localAccount(localAccount)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }
}
