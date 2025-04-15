package dev.book.user.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserProfileResponse getUserProfile(CustomUserDetails userDetails) {
        return UserProfileResponse.fromEntity(userDetails.user());
    }
}
