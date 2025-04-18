package dev.book.challenge.api;

import dev.book.challenge.dto.request.ChallengeInviteRequest;
import dev.book.challenge.dto.response.ChallengeInviteResponse;
import dev.book.challenge.dto.response.ErrorResponse;
import dev.book.global.config.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "챌린지 초대 관련 API", description = "챌린지 초대,초대목록,수락 및 거절 API ")
public interface ChallengeInviteApi {

    @Operation(summary = "챌린지 초대 API ", description = "사용자가 사용자를 초대합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 초대 성공."),
            @ApiResponse(responseCode = "409", description = "챌린지 초대 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 챌린지에 초대 되었습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<String> invite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChallengeInviteRequest challengeInviteRequest);


    @Operation(summary = "챌린지 초대 목록 API ", description = "상대방이 초대한 목록을 조회 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 조회 성공."),
    })
    ResponseEntity<List<ChallengeInviteResponse>> findInviteList(@AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "챌린지 초대 수락 API ", description = "초대를 수락 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 초대 수락"),
            @ApiResponse(responseCode = "404", description = "유효한 초대를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> acceptInvite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "챌린지 초대 거절 API ", description = "초대를 거절 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 초대 거절"),
            @ApiResponse(responseCode = "404", description = "유효한 초대를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> rejectInvite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails);
}
