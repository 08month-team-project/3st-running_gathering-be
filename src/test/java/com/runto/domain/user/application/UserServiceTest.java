package com.runto.domain.user.application;

import com.runto.domain.user.dao.DeactivateUserRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.DeactivateUser;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.dto.CheckEmailRequest;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.security.detail.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeactivateUserRepository deactivateUserRepository;

    private SignupRequest requestUser;

    private User user;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = mock(CustomUserDetails.class);

        requestUser = SignupRequest.builder()
                .email("testEmail@email.com")
                .password("asdf1234")
                .nickname("TEST")
                .build();
        String encodedPassword = bCryptPasswordEncoder.encode(requestUser.getPassword());

        user = User.of("testEmail@email.com","TEST")
                .withLocalAccount(encodedPassword);
    }

    @Test
    @DisplayName("회원가입")
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(requestUser);

        assertThat(user.getEmail()).isEqualTo(requestUser.getEmail());
    }

    @Test
    @DisplayName("중복 회원 예외")
    void checkUserDuplicate() {
        when(userRepository.findByEmail(eq(requestUser.getEmail()))).thenReturn(Optional.of(user));

        assertThrows(UserException.class,()->userService.createUser(requestUser));
    }

    @Test
    @DisplayName("이메일 중복")
    void checkEmailDuplicate() {
        CheckEmailRequest checkEmailRequest = CheckEmailRequest.builder()
                .email("testEmail@email.com")
                .build();

        when(userRepository.findByEmail(eq(requestUser.getEmail()))).thenReturn(Optional.of(user));
        assertThrows(UserException.class,()->userService.checkEmailDuplicate(checkEmailRequest));
    }

    @Test
    @DisplayName("회원탈퇴")
    void deactivateUser() {

        when(userDetails.getUserId()).thenReturn(1L); // 유저 ID 모킹
        when(userDetails.getUsername()).thenReturn("test@example.com"); // 이메일 모킹
        when(userRepository.findById(userDetails.getUserId())).thenReturn(Optional.of(user));

        // Act
        userService.deactivateUser(userDetails);

        // Assert
        verify(deactivateUserRepository, times(1)).save(any(DeactivateUser.class));
        verify(userRepository, times(1)).delete(user);
    }
}