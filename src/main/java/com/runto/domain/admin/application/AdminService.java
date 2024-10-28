package com.runto.domain.admin.application;

import com.runto.domain.admin.dto.MonthUserResponse;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;

    public List<MonthUserResponse> getUserByMonth(UserStatus status) {
        return userRepository.findAllByUserByMonth(status);
    }
}
