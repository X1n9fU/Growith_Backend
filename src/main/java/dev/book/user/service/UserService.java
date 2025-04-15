package dev.book.user.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.service.AuthService;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserProfileResponse getUserProfile(CustomUserDetails userDetails) {
        return UserProfileResponse.fromEntity(userDetails.user());
    }

    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileUpdateRequest profileUpdateRequest, CustomUserDetails userDetails) {

        authService.validateNickname(profileUpdateRequest.nickname());

        UserEntity user = userDetails.user();
        user.updateNickname(profileUpdateRequest.nickname());
        user.updateProfileImage(profileUpdateRequest.profileImageUrl());
        userRepository.save(user);

        return UserProfileResponse.fromEntity(user);
    }

    @Transactional
    public void deleteUser(CustomUserDetails userDetails) {
        userRepository.delete(userDetails.user());
        //todo 연관된 entity 대하여 삭제 처리
    }
}
