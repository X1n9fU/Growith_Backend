package dev.book.user_friend.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user_friend.dto.response.InvitingUserTokenResponseDto;
import dev.book.user_friend.dto.response.KakaoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "친구 API", description = "친구 초대 토큰 발급, 친구 조회, 삭제 api")
public interface UserFriendSwaggerController {

    @Operation(summary = "친구 초대 토큰 발급", description = "`카카오톡 공유하기` 전 친구 초대 토큰을 발급 후 포함하여 `공유하기`를 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 초대 토큰 발급 완료",
                    content = @Content(schema = @Schema(implementation = InvitingUserTokenResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "토큰 암호화에 실패하였습니다"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.")
    })
    ResponseEntity<InvitingUserTokenResponseDto> getInviteUserToken(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception;

    @Operation(summary= "카카오톡 웹훅용 (클라이언트 쪽 사용 X)", description = "카카오톡 웹 훅을 통해 `공유하기`가 성공했다는 것을 전달받는 api")
    ResponseEntity<?> getWebHookFromKakao(@RequestBody KakaoResponseDto kakaoResponseDto) throws Exception;

    @Operation(summary = "`카카오톡 공유하기` 링크", description = "`카카오톡 공유하기` 통해 접근하는 URL, 친구 초대 토큰을 통해 친구 요청을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 요청 생성 완료"),
            @ApiResponse(responseCode = "404", description = "초대 요청을 한 내역을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "초대 요청을 받은 유저를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "토큰 복호화에 실패하였습니다"),
    })
    ResponseEntity<?> getTokenAndMakeInvitation(HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @RequestParam(name="token") String token) throws Exception;

}
