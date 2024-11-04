package com.runto.domain.admin.application;

import com.runto.domain.admin.dto.PenaltyDetailsResponse;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.type.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService; // 테스트할 서비스 클래스

    @Mock
    private UserRepository userRepository; // Mock 객체

    private UserStatus status = UserStatus.BANNED;
    private Pageable pageable = PageRequest.of(1, 5);


    @Test
    public void testGetPenaltiesByUser() {
        for(int i = 0; i < 10; i++) {
            User user = User.builder()
                    .nickname("nickname"+i)
                    .email("email@email.com"+i)
                    .status(UserStatus.BANNED)
                    .build();
            userRepository.save(user);
        }
        List<PenaltyDetailsResponse> responses = List.of(
                new PenaltyDetailsResponse("nickname", "email@email.com", 1L)
        );
        Page<PenaltyDetailsResponse> pageResponse = new PageImpl<>(responses);

        when(userRepository.findAllByPenalties(status, pageable)).thenReturn(pageResponse);

        List<PenaltyDetailsResponse> result = adminService.getPenaltiesByUser(status, pageable);

        assertNotNull(result);
        assertEquals("nickname", result.get(0).getNickname());
    }
}