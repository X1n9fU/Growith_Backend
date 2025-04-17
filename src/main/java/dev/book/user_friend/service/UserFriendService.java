package dev.book.user_friend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.book.user_friend.dto.EncryptUserInfo;
import dev.book.user_friend.dto.response.KakaoResponseDto;
import dev.book.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user_friend.util.AESUtil;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import dev.book.user_friend.entity.UserFriend;
import dev.book.user_friend.repository.UserFriendRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserFriendService {

    private static final String MAIN_URL = "http://localhost:8080/main";

    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final ObjectMapper objectMapper;

    public InvitingUserTokenResponseDto getInviteUserToken(CustomUserDetails userDetails) throws Exception {
        Long id = userDetails.user().getId();
        String email = userDetails.getUsername();
        EncryptUserInfo encryptUserInfo = new EncryptUserInfo(email, id, LocalDateTime.now());
        String token = AESUtil.encrypt(objectMapper.writeValueAsString(encryptUserInfo));
        String safeToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return new InvitingUserTokenResponseDto(safeToken);
    }

    public void getWebHookFromKakao(KakaoResponseDto kakaoResponseDto) throws Exception {
        String requestUserToken = kakaoResponseDto.requestUserToken();
        EncryptUserInfo userInfo = decryptToken(requestUserToken);
        UserEntity invitingUser = userRepository.findById(userInfo.id())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        userFriendRepository.save(UserFriend.of(invitingUser, userInfo.localDateTime()));
    }

    @Transactional
    public void getTokenAndMakeInvitation(CustomUserDetails userDetails, HttpServletResponse response, String token) throws Exception {
        makeInvitation(userDetails.getUsername(), token);
        response.sendRedirect(MAIN_URL);
    }

    public void makeInvitation(String email, String safeToken) throws Exception {
        String token = URLEncoder.encode(safeToken, StandardCharsets.UTF_8);
        EncryptUserInfo userInfo = decryptToken(token);
        UserFriend userFriend = userFriendRepository.findByInvitingUserAndRequestedAt(userInfo.id(), userInfo.localDateTime())
                .orElseThrow(() -> new UserErrorException(UserErrorCode.INVITING_NOT_FOUND));
        UserEntity friend = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        userFriend.inviteFriend(friend);
    }

    private EncryptUserInfo decryptToken(String safeToken) throws Exception {
        String token = URLDecoder.decode(safeToken, StandardCharsets.UTF_8);
        String decryptUserInfo = AESUtil.decrypt(token);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(decryptUserInfo, EncryptUserInfo.class);
    }

}
