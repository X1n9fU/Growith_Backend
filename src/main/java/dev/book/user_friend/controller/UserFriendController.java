package dev.book.user_friend.controller;

import dev.book.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user_friend.dto.response.KakaoResponseDto;
import dev.book.user_friend.service.UserFriendService;
import dev.book.global.config.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class UserFriendController implements UserFriendSwaggerController{

    private final UserFriendService userFriendService;

    @GetMapping("/invite/token")
    public ResponseEntity<InvitingUserTokenResponseDto> getInviteUserToken(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(userFriendService.getInviteUserToken(userDetails));
    }

    //kakao 서버에서 도착한 웹 훅
    @PostMapping("/kakao")
    public ResponseEntity<?> getWebHookFromKakao(@RequestBody KakaoResponseDto kakaoResponseDto) throws Exception {
        userFriendService.getWebHookFromKakao(kakaoResponseDto);
        return ResponseEntity.ok().build();
    }

    //인증된 유저가 초대 메세지를 받았을 경우 토큰 처리 진행
    @GetMapping("/invite")
    public ResponseEntity<?> getTokenAndMakeInvitation(HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam(name="token") String token) throws Exception {
        userFriendService.getTokenAndMakeInvitation(userDetails, response, token);
        return ResponseEntity.ok().build();
    }

}
