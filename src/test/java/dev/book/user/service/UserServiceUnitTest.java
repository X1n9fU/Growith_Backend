package dev.book.user.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import dev.book.util.UserBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private JwtUtil jwtUtil;

    CustomUserDetails userDetails;

    @BeforeEach
    public void createUser(){
        UserEntity user = UserBuilder.of();
        userDetails = new CustomUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }


    @Test
    @DisplayName("유저의 프로필을 가져온다.")
    void getUserProfile() {
        //give
        UserEntity user = userDetails.user();
        // when
        UserProfileResponse userProfileResponse = userService.getUserProfile(userDetails);

        //then
        assertThat(userProfileResponse).isNotNull();
        assertThat(userProfileResponse.email()).isEqualTo(user.getEmail());
        assertThat(userProfileResponse.nickname()).isEqualTo(user.getNickname());
        assertThat(userProfileResponse.profileImageUrl()).isEqualTo(user.getProfileImageUrl());
    }

    @Test
    @DisplayName("유저의 프로필을 업데이트한다.")
    void updateUserProfile() {
        //given
        UserEntity user = userDetails.user();
        String changeNickname = "change";
        String changeProfile = "newProfile";
        UserProfileUpdateRequest userProfileUpdateRequest = new UserProfileUpdateRequest(changeNickname, changeProfile);
        given(userRepository.save(any(UserEntity.class))).willReturn(userDetails.user());

        //when
        UserProfileResponse userProfileResponse = userService.updateUserProfile(userProfileUpdateRequest, userDetails);

        //then
        assertThat(userProfileResponse).isNotNull();
        assertThat(userProfileResponse.email()).isEqualTo(user.getEmail());
        assertThat(userProfileResponse.nickname()).isEqualTo(changeNickname);
        assertThat(userProfileResponse.profileImageUrl()).isEqualTo(changeProfile);
    }

    @Test
    @DisplayName("닉네임이 중복된다면 에러가 발생한다.")
    void failUpdateUserProfile(){
        // given
        String duplicateNickname = "중복닉네임";
        String changeProfile = "newProfile";
        UserProfileUpdateRequest userProfileUpdateRequest = new UserProfileUpdateRequest(duplicateNickname, changeProfile);

        doThrow(new UserErrorException(UserErrorCode.DUPLICATE_NICKNAME))
                .when(userService).validateNickname(duplicateNickname);

        // when & then
        assertThatThrownBy(() -> userService.updateUserProfile(userProfileUpdateRequest, userDetails))
                .isInstanceOf(UserErrorException.class)
                .hasMessageContaining(UserErrorCode.DUPLICATE_NICKNAME.getMessage());

        verify(userService).validateNickname(duplicateNickname);
        verify(userRepository, never()).save(any());

    }


    @Test
    @DisplayName("유저를 삭제한다.")
    void deleteUser() {
        //when
        userService.deleteUser(request, response, userDetails);

        //then
        verify(jwtUtil).deleteAccessTokenAndRefreshToken(request, response);
        verify(userRepository).delete(userDetails.user());
    }

    @Test
    @DisplayName("유저 정보가 없다면 에러가 발생한다.")
    void failDeleteUser(){

        //when, then
        assertThatThrownBy(() -> userService.deleteUser(request, response, null))
                .isInstanceOf(NullPointerException.class);
    }
}