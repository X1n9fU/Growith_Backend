package dev.book.user.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.dto.request.UserCategoriesRequest;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User API", description = "유저 프로필 조회, 수정, 유저 삭제 api")
public interface UserSwaggerController {

    @Operation(summary = "유저 삭제(회원 탈퇴)", description = "현재 로그인된 유저를 탈퇴시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 삭제 완료"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.")
    })
    ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "유저 프로필 반환", description = "현재 로그인된 유저의 프로필을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 프로필 정보 반환 완료",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.")
    })
    ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "유저 프로필 수정", description = "현재 로그인된 유저의 프로필을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 프로필 수정 완료",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임입니다"),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.")
    })
    ResponseEntity<UserProfileResponse> updateUserProfile(@RequestBody UserProfileUpdateRequest profileUpdateRequest,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "유저 카테고리 수정", description = "현재 로그인된 유저의 카테고리를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 카테고리 수정 완료"),
            @ApiResponse(responseCode = "400", description = "일치하지 않는 카테고리가 존재합니다."),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.")
    })
    ResponseEntity<?> updateUserCategories(@RequestBody UserCategoriesRequest userCategoriesRequest,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails);
}

