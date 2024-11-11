package com.runto.domain.user.application;

import com.runto.domain.image.application.ImageService;
import com.runto.domain.image.dto.ImageUrlDto;
import com.runto.domain.user.dao.DeactivateUserRepository;
import com.runto.domain.user.dao.UserRepository;
import com.runto.domain.user.domain.DeactivateUser;
import com.runto.domain.user.domain.User;
import com.runto.domain.user.dto.CheckEmailRequest;
import com.runto.domain.user.dto.SignupRequest;
import com.runto.domain.user.dto.UserProfileResponse;
import com.runto.domain.user.excepction.UserException;
import com.runto.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.runto.domain.user.type.UserStatus.ACTIVE;
import static com.runto.global.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageService imageService;
    private final DeactivateUserRepository deactivateUserRepository;

    @Transactional
    public void createUser(SignupRequest signupRequest) {
        userRepository.findByNickname(signupRequest.getNickname())
                .ifPresent(user -> {
                    throw new UserException(ALREADY_EXIST_NICKNAME);
                });
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(user -> {
                    throw new UserException(ALREADY_EXIST_USER);
                });

        String encodedPwd = bCryptPasswordEncoder.encode(signupRequest.getPassword());
        String nickname = signupRequest.getNickname();
        String email = signupRequest.getEmail();

        User user = User.of(email, nickname)
                .withLocalAccount(encodedPwd);

        userRepository.save(user);
    }

    public void checkEmailDuplicate(CheckEmailRequest checkEmailRequest) {
        Optional<User> findUser = userRepository.findByEmail(checkEmailRequest.getEmail());

        if (findUser.isPresent()) {
            throw new UserException(ALREADY_EXIST_USER);
        }
    }

    public UserProfileResponse getUserProfile(Long userId) {

        return UserProfileResponse.from(
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserException(USER_NOT_FOUND)));
    }

    @Transactional
    public void updateUserNickname(String nickname, Long userId) {

        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(ALREADY_EXIST_NICKNAME);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        user.updateNickname(nickname); // 정상회원이 아닐 경우 수정불가
    }

    @Transactional
    public ImageUrlDto updateUserProfile(Long userId, MultipartFile multipartFile) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // s3에 업로드 시도 전 유저상태 검증 필요
        if (!ACTIVE.equals(user.getStatus())) {
            throw new UserException(INVALID_PROFILE_UPDATE_INACTIVE_USER);
        }

        ImageUrlDto imageUrlDto = imageService
                .updateUserProfile(userId, multipartFile, user.getProfileImageUrl());

        user.updateProfile(imageUrlDto.getImageUrl());
        return imageUrlDto;
    }

    @Transactional
    public void deactivateUser(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        String email = userDetails.getUsername();

        DeactivateUser deactivateUser = DeactivateUser.from(email);

        deactivateUserRepository.save(deactivateUser);

        userRepository.delete(user);
    }
}
