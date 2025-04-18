package dev.book.global.config.security.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.dto.request.UserSignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag( name = "Auth API", description = "회원가입, 로그아웃, 토큰 재발급 api")
public interface AuthSwaggerController {

    @Operation(summary = "회원가입 진행", description = "OAuth2 로그인 후 새로 등록된 유저의 나머지 사항(nickname, category)를 입력받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "유저 회원가입 완료"),
            @ApiResponse(responseCode = "404", description = "OAuth2 로그인이 완료되지 않은 유저가 회원가입을 진행하려고 할 시에 유저를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임입니다")
    })
    ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignupRequest,
                                     @RequestParam("email") @Email String email, HttpServletResponse response);

    @Operation(summary = "로그아웃 진행", description = "존재하는 JWT 토큰 쿠키를 모두 삭제하고 저장된 RefreshToken을 삭제하여 로그아웃을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 진행 완료"),
    })
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,
                                    @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "Access 토큰 재발급 진행", description = "쿠키에 존재하는 RefreshToken을 통해 Access, RefreshToken을 재발급 받아 쿠키에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access, RefreshToken 재발급 완료"),
            @ApiResponse(responseCode = "401", description = "RefreshToken도 만료되었습니다. 다시 로그인을 진행합니다."),
            @ApiResponse(responseCode = "401", description = "RefreshToken이 존재하지 않거나 잘못된 JWT 토큰 형식입니다.")

    })
    ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response);
}
