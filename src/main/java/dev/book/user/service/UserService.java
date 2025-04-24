package dev.book.user.service;

import dev.book.achievement.achievement_user.IndividualAchievementStatusService;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.global.entity.Category;
import dev.book.global.exception.category.CategoryErrorCode;
import dev.book.global.exception.category.CategoryException;
import dev.book.global.repository.CategoryRepository;
import dev.book.user.dto.request.UserCategoriesRequest;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RefreshTokenService refreshTokenService;
    private final IndividualAchievementStatusService individualAchievementStatusService;
    private final JwtUtil jwtUtil;

    public UserProfileResponse getUserProfile(CustomUserDetails userDetails) {
        return UserProfileResponse.fromEntity(userDetails.user());
    }

    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileUpdateRequest profileUpdateRequest, CustomUserDetails userDetails) {

        validateNickname(profileUpdateRequest.nickname());

        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        user.updateNickname(profileUpdateRequest.nickname());
        user.updateProfileImage(profileUpdateRequest.profileImageUrl());
        return UserProfileResponse.fromEntity(user);
    }

    public void validateNickname(String nickname) {
        boolean isExisted = userRepository.existsByNickname(nickname);
        if (isExisted) throw new UserErrorException(UserErrorCode.DUPLICATE_NICKNAME);
    }

    /**
     * Cookie에 설정된 AcessToken, RefreshToken 삭제
     * DB에서 유저 완전히 삭제
     * @param request
     * @param response
     * @param userDetails
     */
    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response, CustomUserDetails userDetails) {
        UserEntity user = userDetails.user();
        refreshTokenService.deleteRefreshToken(user);
        userRepository.delete(user);
        jwtUtil.deleteAccessTokenAndRefreshToken(request, response);
    }

    /**
     * 유저의 카테고리 변경
     * @param userCategoriesRequest
     * @param userDetails
     */
    @Transactional
    public void updateUserCategories(UserCategoriesRequest userCategoriesRequest, CustomUserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        checkCategoriesAndUpdate(userCategoriesRequest.categories(), user);
    }

    public void checkCategoriesAndUpdate(List<String> categories, UserEntity user) {
        List<Category> categoryList = categoryRepository.findByCategoryIn(categories);
        if (categoryList.size() != categories.size())
            throw new CategoryException(CategoryErrorCode.CATEGORY_BAD_REQUEST);
        user.updateCategory(categoryList);
    }

    public void checkIsUserLogin(CustomUserDetails userDetails) {
        individualAchievementStatusService.deterMineContinuous(userDetails.user());
    }

    public void checkIsValidateNickname(String nickname) {
        validateNickname(nickname);
    }

    @Transactional
    public void deleteUserNickname(CustomUserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        user.deleteNickname();
    }
}
