package dev.book.user.user_friend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.book.achievement.achievement_user.dto.event.InviteFriendToServiceEvent;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import dev.book.user.user_friend.dto.EncryptUserInfo;
import dev.book.user.user_friend.dto.response.FriendListResponseDto;
import dev.book.user.user_friend.dto.response.FriendRequestListResponseDto;
import dev.book.user.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user.user_friend.dto.response.KakaoResponseDto;
import dev.book.user.user_friend.entity.UserFriend;
import dev.book.user.user_friend.repository.UserFriendRepository;
import dev.book.user.user_friend.util.AESUtil;
import dev.book.util.UserBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserFriendServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFriendRepository userFriendRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    UserFriendService userFriendService;

    @Spy
    @InjectMocks
    AESUtil aesUtil;

    CustomUserDetails userDetails;

    @BeforeEach
    public void createUser(){
        UserEntity user = UserBuilder.of();
        userDetails = new CustomUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    @BeforeEach
    void setUp(){
        objectMapper.registerModule(new JavaTimeModule());
        ReflectionTestUtils.setField(aesUtil, "SECRET_KEY", "abcdefghijklmnopqrstuvwxyzabcdef");
    }

    @Test
    @DisplayName("유저 초대 토큰을 생성한다.")
    void getInviteUserToken() throws Exception {
        //given
        String jsonToken = makeJsonTokenFromEncryptUserInfo();
        given(objectMapper.writeValueAsString(any(EncryptUserInfo.class))).willReturn(jsonToken);

        //when
        InvitingUserTokenResponseDto invitingUserTokenResponseDto = userFriendService.getInviteUserToken(userDetails);

        //then
        assertThat(invitingUserTokenResponseDto).isNotNull();
    }

    private String makeJsonTokenFromEncryptUserInfo() throws JsonProcessingException {
        EncryptUserInfo encryptUserInfo = new EncryptUserInfo(userDetails.getUsername(), 1L, LocalDateTime.of(2025, 4, 21, 14, 29, 54));
        ObjectMapper newObjectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return newObjectMapper.writeValueAsString(encryptUserInfo);
    }

    @NotNull
    private InvitingTokenAndJson getInvitingTokenAndJson() throws Exception {
        String jsonToken = makeJsonTokenFromEncryptUserInfo();
        given(objectMapper.writeValueAsString(any(EncryptUserInfo.class))).willReturn(jsonToken);
        InvitingUserTokenResponseDto invitingUserTokenResponseDto = userFriendService.getInviteUserToken(userDetails);
        return new InvitingTokenAndJson(jsonToken, invitingUserTokenResponseDto);
    }

    private record InvitingTokenAndJson(String jsonToken, InvitingUserTokenResponseDto invitingUserTokenResponseDto) {
    }

    @Test
    @DisplayName("공유하기를 성공했을 때 카카오 서버에서 오는 웹훅으로 초대 내역을 생성한다.")
    void getWebHookFromKakao() throws Exception {
        //given
        //토큰 생성
        EncryptUserInfo encryptUserInfo = new EncryptUserInfo(userDetails.getUsername(), null, LocalDateTime.of(2025, 4, 21, 14, 29, 54));
        InvitingTokenAndJson invitingTokenAndJson = getInvitingTokenAndJson();

        given(objectMapper.readValue(eq(invitingTokenAndJson.jsonToken()), eq(EncryptUserInfo.class))).willReturn(encryptUserInfo);
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(userDetails.user()));

        //when, 사전에 생성한 토큰으로 받은 웹훅
        KakaoResponseDto kakaoResponseDto = new KakaoResponseDto("chat", 1000L, "hash", invitingTokenAndJson.invitingUserTokenResponseDto().invitingUserToken());
        userFriendService.getWebHookFromKakao(kakaoResponseDto);

        //then
        verify(userFriendRepository).save(argThat(userFriend ->
                userFriend.getUser().equals(userDetails.user()) &&
                userFriend.getRequestedAt().equals(encryptUserInfo.localDateTime())));
    }

    @Test
    @WithMockUser("test@test.com")
    @DisplayName("초대 토큰을 받아 초대 내역을 완성한다.")
    void getTokenAndMakeInvitation() throws Exception {
        //given
        //초대 토큰 생성
        EncryptUserInfo encryptUserInfo = new EncryptUserInfo(userDetails.getUsername(), 1L, LocalDateTime.of(2025, 4, 21, 14, 29, 54));
        InvitingTokenAndJson invitingTokenAndJson = getInvitingTokenAndJson();

        //토큰 받은 유저
        UserEntity invitedUser = UserBuilder.of("test2@test.com");
        userDetails = new CustomUserDetails(invitedUser);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));

        UserFriend userFriend = mock(UserFriend.class);
        given(objectMapper.readValue(eq(invitingTokenAndJson.jsonToken()), eq(EncryptUserInfo.class))).willReturn(encryptUserInfo);
        given(userFriendRepository.findByInvitingUserAndRequestedAt(any(), any())).willReturn(Optional.ofNullable(userFriend));
        given(userRepository.findByEmail(invitedUser.getEmail())).willReturn(Optional.of(invitedUser));

        //when
        userFriendService.getTokenAndMakeInvitation(userDetails, response, invitingTokenAndJson.invitingUserTokenResponseDto.invitingUserToken());

        //then
        verify(userFriend).inviteFriend(invitedUser);
        verify(eventPublisher).publishEvent(any(InviteFriendToServiceEvent.class));
    }


    @Test
    @DisplayName("친구 요청 내역을 반환한다.")
    void getFriendRequestList() {
        //given
        //토큰을 만들어서 보낸 유저 (초대한 유저)
        UserEntity invitingUser = UserBuilder.of("test2@test.com");
        given(userFriendRepository.findAllByInvitedUserAndIsRequestIsTrue(any())).willReturn(List.of(invitingUser));

        //when
        List<FriendRequestListResponseDto> requestListResponseDtos = userFriendService.getFriendRequestList(userDetails);

        //then
        assertThat(requestListResponseDtos.size()).isEqualTo(1);
        assertThat(requestListResponseDtos.get(0).email()).isEqualTo(invitingUser.getEmail());

        verify(userFriendRepository).findAllByInvitedUserAndIsRequestIsTrue(any());
    }

    @Test
    @DisplayName("친구 요청을 승낙한다.")
    void acceptFriend() {
        //given
        UserEntity invitingUser = UserBuilder.of("test2@test.com");
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(invitingUser));

        UserFriend userFriend = mock(UserFriend.class);
        given(userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(invitingUser, userDetails.user())).willReturn(Optional.ofNullable(userFriend));

        //when
        userFriendService.acceptFriend(userDetails, any());

        //then
        verify(userFriend).accept();
    }

    @Test
    @DisplayName("친구 목록을 반환한다.")
    void getFriendList() {
        //given
        //토큰을 만들어서 보낸 유저 (초대한 유저)
        UserEntity invitingUser = UserBuilder.of("test2@test.com");
        given(userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(any())).willReturn(List.of(invitingUser));

        //when
        List<FriendListResponseDto> friendListResponseDtos = userFriendService.getFriendList(userDetails);

        //then
        assertThat(friendListResponseDtos.size()).isEqualTo(1);
        assertThat(friendListResponseDtos.get(0).name()).isEqualTo(invitingUser.getName());

        verify(userFriendRepository).findAllByInvitedUserAndIsAcceptIsTrue(any());
    }

    @Test
    @DisplayName("친구 요청을 거절한다.")
    void rejectFriend() {
        //given
        UserEntity invitingUser = UserBuilder.of("test2@test.com");
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(invitingUser));

        UserFriend userFriend = mock(UserFriend.class);
        given(userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(invitingUser, userDetails.user())).willReturn(Optional.ofNullable(userFriend));

        //when
        userFriendService.rejectFriend(userDetails, any());

        //then
        verify(userFriendRepository).delete(any(UserFriend.class));
    }

    @Test
    @DisplayName("친구를 삭제한다.")
    void deleteFriend() {
        //given
        UserEntity friend = UserBuilder.of("test2@test.com");
        given(userRepository.findById(any())).willReturn(Optional.ofNullable(friend));

        //when
        userFriendService.deleteFriend(userDetails, any());

        //then
        verify(userFriendRepository).deleteByUserAndFriendAndIsAcceptIsTrue(userDetails.user(), friend);
        verify(userFriendRepository).deleteByUserAndFriendAndIsAcceptIsTrue(friend, userDetails.user());
    }
}