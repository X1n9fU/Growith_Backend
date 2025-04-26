package dev.book.user.user_friend.controller;

import dev.book.user.user_friend.controller.swagger.UserFriendApi;
import dev.book.user.user_friend.dto.response.FriendListResponseDto;
import dev.book.user.user_friend.dto.response.FriendRequestListResponseDto;
import dev.book.user.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user.user_friend.dto.response.KakaoResponseDto;
import dev.book.user.user_friend.service.UserFriendService;
import dev.book.global.config.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class UserFriendController implements UserFriendApi {

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

    @GetMapping("/list")
    public ResponseEntity<List<FriendListResponseDto>> getFriendList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok()
                .body(userFriendService.getFriendList(userDetails));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestListResponseDto>> getFriendRequestList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok()
                .body(userFriendService.getFriendRequestList(userDetails));
    }

    @GetMapping("/request/{friend_id}/accept")
    public ResponseEntity<?> acceptFriend(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("friend_id") Long friendId){
        userFriendService.acceptFriend(userDetails, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/request/{friend_id}/reject")
    public ResponseEntity<?> rejectFriend(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("friend_id") Long friendId){
        userFriendService.rejectFriend(userDetails, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{friend_id}")
    public ResponseEntity<?> deleteFriend(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("friend_id") Long friendId){
        userFriendService.deleteFriend(userDetails, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{friend_id}")
    public ResponseEntity<?> getFriendProfile(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("friend_id") Long friendId){
        return null; //todo 친구 상세 조회
    }

}
