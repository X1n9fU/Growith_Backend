package dev.book.challenge.api;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.*;
import dev.book.global.config.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "챌린지 API", description = "챌린지에 생성, 조회, 수정 및 참여 관련 API ")
public interface ChallengeApi {
    @Operation(summary = "챌린지 생성 API ", description = "사용자가 새로운 챌린지를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "챌린지 생성 성공.")
    })
    ResponseEntity<ChallengeCreateResponse> createChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody ChallengeCreateRequest challengeCreateRequest);

    @Operation(summary = "챌린지 조회 API ", description = "제목또는 내용으로 챌린지를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 검색 성공.")
    })
    ResponseEntity<Page<ChallengeReadResponse>> searchChallenge(@RequestParam(required = false) String title,
                                                                @RequestParam(required = false) String text,
                                                                @RequestParam(required = false, defaultValue = "1") int page,
                                                                @RequestParam(required = false, defaultValue = "10") int size);

    @Operation(summary = "챌린지 상세 조회 API ", description = "챌린지 ID로 챌린지를 조회 합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 검색 성공."),
            @ApiResponse(responseCode = "404", description = "챌린지를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ChallengeReadDetailResponse> searchChallengeById(@PathVariable Long id);

    @Operation(summary = "챌린지 수정 API ", description = "챌린지 ID로 챌린지를 수정 합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 수정 성공."),
            @ApiResponse(responseCode = "403", description = "챌린지 수정 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<ChallengeUpdateResponse> updateChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @Valid @RequestBody ChallengeUpdateRequest challengeUpdateRequest);


    @Operation(summary = "챌린지 삭제 API ", description = "챌린지 ID로 챌린지를 삭제 합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 삭제 성공."),
            @ApiResponse(responseCode = "403", description = "챌린지 삭제 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<?> deleteChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id);

    @Operation(summary = "챌린지 참여 API ", description = "사용자는 챌린지를 참여 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 참여 성공."),
            @ApiResponse(responseCode = "403", description = "챌린지 삭제 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "챌린지 인원을 초과 하였습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<String> participate(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id);

    @Operation(summary = "챌린지 퇴장 API ", description = "사용자는 챌린지를 퇴장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 퇴장 성공."),
            @ApiResponse(responseCode = "404", description = "사용자가 속한 챌린지가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<String> leaveChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id);

    @Operation(summary = "인기 챌린지 조회 API ", description = "모집중인 챌린지 중 상위 3개를 조회 합니다.")
    ResponseEntity<List<ChallengeTopResponse>> topChallenge();
}
