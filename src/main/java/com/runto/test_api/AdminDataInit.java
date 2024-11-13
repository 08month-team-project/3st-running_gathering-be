package com.runto.test_api;

import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.LocalAccount;
import com.runto.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.runto.domain.user.type.Gender.NONE;
import static com.runto.domain.user.type.UserRole.ADMIN;

@RequiredArgsConstructor
//@Component
public class AdminDataInit {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {

        String password = bCryptPasswordEncoder.encode("qweasdzxc123");

        LocalAccount localAccount =LocalAccount.builder()
                .password(password)
                .build();

        User user = User.builder()
                .email("admin@runto.com")
                .name("관리자다")
                .nickname("관리자다")
                .gender(NONE)
                .localAccount(localAccount)
                .role(ADMIN)
                .build();

        userRepository.save(user);
    }
}
