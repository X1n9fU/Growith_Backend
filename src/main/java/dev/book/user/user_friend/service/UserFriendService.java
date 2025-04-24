package dev.book.user.user_friend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.book.achievement.achievement_user.dto.event.InviteFriendToServiceEvent;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import dev.book.user.user_friend.dto.EncryptUserInfo;
import dev.book.user.user_friend.dto.response.FriendListResponseDto;
import dev.book.user.user_friend.dto.response.FriendRequestListResponseDto;
import dev.book.user.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user.user_friend.dto.response.KakaoResponseDto;
import dev.book.user.user_friend.entity.UserFriend;
import dev.book.user.user_friend.exception.UserFriendErrorCode;
import dev.book.user.user_friend.exception.UserFriendException;
import dev.book.user.user_friend.repository.UserFriendRepository;
import dev.book.user.user_friend.util.AESUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFriendService {

//    @Value("${springdoc.servers.production.url}")
    private String DOMAIN = "http://localhost:3000";
    private final String MAIN_URL = "/main";

    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    public InvitingUserTokenResponseDto getInviteUserToken(CustomUserDetails userDetails) throws Exception {
        Long id = userDetails.user().getId();
        String email = userDetails.getUsername();
        EncryptUserInfo encryptUserInfo = new EncryptUserInfo(email, id, LocalDateTime.now());
        String token = AESUtil.encrypt(objectMapper.writeValueAsString(encryptUserInfo));
        String safeToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return new InvitingUserTokenResponseDto(safeToken);
    }

    public void getWebHookFromKakao(KakaoResponseDto kakaoResponseDto) throws Exception {
        String requestUserToken = kakaoResponseDto.invitingUserToken();
        EncryptUserInfo userInfo = decryptToken(requestUserToken);
        UserEntity invitingUser = userRepository.findById(userInfo.id())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        userFriendRepository.save(UserFriend.of(invitingUser, userInfo.localDateTime()));
    }

    @Transactional
    public void getTokenAndMakeInvitation(CustomUserDetails userDetails, HttpServletResponse response, String token) throws Exception {
        invitedUserMakeInvitation(userDetails.getUsername(), token);
        response.sendRedirect(DOMAIN+MAIN_URL);
    }

    public void invitedUserMakeInvitation(String email, String token) throws Exception {
        EncryptUserInfo userInfo = decryptToken(token);
        UserFriend userFriend = userFriendRepository.findByInvitingUserAndRequestedAt(userInfo.id(), userInfo.localDateTime())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.INVITING_NOT_FOUND));
        UserEntity friend = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        userFriend.inviteFriend(friend);
        eventPublisher.publishEvent(new InviteFriendToServiceEvent(userFriend.getUser()));
    }

    private EncryptUserInfo decryptToken(String safeToken) throws Exception {
        String token = URLDecoder.decode(safeToken, StandardCharsets.UTF_8);
        String decryptUserInfo = AESUtil.decrypt(token);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(decryptUserInfo, EncryptUserInfo.class);
    }

    public List<FriendListResponseDto> getFriendList(CustomUserDetails userDetails) {
        UserEntity invitedUser = userDetails.user();
        //친구 요청이 승낙된 내역만
        List<UserEntity> friendLists = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(invitedUser.getId());
        return friendLists.stream()
                .map(FriendListResponseDto::of)
                .toList();
    }

    public List<FriendRequestListResponseDto> getFriendRequestList(CustomUserDetails userDetails) {
        UserEntity invitedUser = userDetails.user();
        //친구 요청이 요청 중인 내역만
        List<UserEntity> friendLists = userFriendRepository.findAllByInvitedUserAndIsRequestIsTrue(invitedUser.getId());
        return friendLists.stream()
                .map(FriendRequestListResponseDto::of)
                .toList();
    }

    @Transactional
    public void acceptFriend(CustomUserDetails userDetails, Long friendId) {
        UserFriend friendRequest = getRequestByUserAndFriend(userDetails.user(), friendId);
        friendRequest.accept();
        userFriendRepository.save(UserFriend.of(friendRequest.getUser(), friendRequest.getFriend()));
    }

    @Transactional
    public void rejectFriend(CustomUserDetails userDetails, Long friendId) {
        UserFriend friendRequest = getRequestByUserAndFriend(userDetails.user(), friendId);
        userFriendRepository.delete(friendRequest);
    }

    private UserFriend getRequestByUserAndFriend(UserEntity user, Long friendId) {
        UserEntity requestFriendUser = userRepository.findById(friendId)
                        .orElseThrow(() -> new UserErrorException(UserErrorCode.FRIEND_NOT_FOUND));
        return userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(requestFriendUser, user)
                .orElseThrow(() -> new UserFriendException(UserFriendErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }


    @Transactional
    public void deleteFriend(CustomUserDetails userDetails, Long friendId) {
        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.FRIEND_NOT_FOUND));
        //양방향 삭제
        userFriendRepository.deleteByUserAndFriendAndIsAcceptIsTrue(userDetails.user(), friend);
        userFriendRepository.deleteByUserAndFriendAndIsAcceptIsTrue(friend, userDetails.user());
    }
}
