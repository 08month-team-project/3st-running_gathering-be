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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    private UserStatus status = UserStatus.BANNED;
    private Pageable pageable = PageRequest.of(0, 5); // 첫 번째 페이지, 한 페이지에 5개씩

    @Test
    public void testGetPenaltiesByUser() {
        // Mock PenaltyDetailsResponse 데이터 생성
        List<PenaltyDetailsResponse> penaltyDetailsResponses = IntStream.range(0, 10)
                .mapToObj(i -> new PenaltyDetailsResponse("nickname" + i, "email" + i + "@example.com", 1L))
                .collect(Collectors.toList());

        // 페이징된 결과를 반환하는 Page 객체 생성
        Page<PenaltyDetailsResponse> penaltyPage = new PageImpl<>(penaltyDetailsResponses.subList(0, 5), pageable, penaltyDetailsResponses.size());

        // findAllByPenalties 메서드의 Mock 동작 설정
        when(userRepository.findAllByPenalties(status, pageable)).thenReturn(penaltyPage);

        // 테스트 실행: AdminService의 getPenaltiesByUser 메서드 호출
        List<PenaltyDetailsResponse> result = adminService.getPenaltiesByUser(status, pageable);

        // 검증
        assertEquals(5, result.size()); // 반환된 결과가 5개인지 확인
        assertEquals("nickname0", result.get(0).getNickname()); // 첫 번째 PenaltyDetailsResponse 닉네임 확인
        assertEquals("nickname4", result.get(4).getNickname()); // 마지막 PenaltyDetailsResponse 닉네임 확인

        // Mock 객체가 예상대로 호출되었는지 검증
        verify(userRepository, times(1)).findAllByPenalties(status, pageable);
    }
}