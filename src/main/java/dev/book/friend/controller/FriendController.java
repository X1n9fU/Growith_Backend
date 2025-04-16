package dev.book.friend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.book.friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.friend.dto.response.KakaoFriendsResponseDto;
import dev.book.friend.dto.response.KakaoResponseDto;
import dev.book.friend.service.FriendService;
import dev.book.global.config.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/request/token")
    public ResponseEntity<InvitingUserTokenResponseDto> getInviteUserToken(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(friendService.getInviteUserToken(userDetails));
    }

    //kakao 서버에서 도착한 웹 훅
    @PostMapping("/kakao")
    public ResponseEntity<?> getWebHookFromKakao(@RequestBody KakaoResponseDto kakaoResponseDto) throws Exception {
        friendService.getWebHookFromKakao(kakaoResponseDto);
        return ResponseEntity.ok().build();
    }

    //인증된 유저가 초대 메세지를 받았을 경우 토큰 처리 진행
    @GetMapping("/request")
    public ResponseEntity<?> getTokenAndMakeInvitation(HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam(name="token") String token) throws Exception {
        friendService.getTokenAndMakeInvitation(userDetails, response, token);
        return ResponseEntity.ok().build();
    }

}
