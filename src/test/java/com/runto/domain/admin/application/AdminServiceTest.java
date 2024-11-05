package com.runto.domain.admin.application;

import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.user.application.UserService;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.Gender;
import com.runto.domain.user.type.UserRole;
import com.runto.domain.user.type.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    final private UserStatus userStatus = UserStatus.BANNED;
    final private Integer pageSize = 2;

    @DisplayName("api 동작확인")
    @Test
    void getPenaltiesByUser(){

            List<PenaltyDetailsResponse> result = adminService.getPenaltiesByUser(userStatus,PageRequest.of(0, pageSize));

            assertNotNull(result);

        assertEquals("page", result.size(), pageSize);
    }
}