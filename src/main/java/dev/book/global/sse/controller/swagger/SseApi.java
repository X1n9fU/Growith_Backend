package dev.book.global.sse.controller.swagger;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.sse.dto.SseAchievementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE API", description = "SSE 구독 경로와 테스트 api")
public interface SseApi {

    @Operation(summary = "SSE 구독 경로", description = "SSE를 구독하는 경로입니다. 구독 후 알림을 받을 수 있습니다. " +
            "\n\n Last-Event-ID 는 마지막에 도착한 event의 값으로 헤더에 넣어 보내주시면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SSE 구독 완료",
                    content = @Content(schema = @Schema(implementation = SseAchievementResponse.class)))
    })
    ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId);

    @Operation(summary = "SSE 테스트 경로", description = "SSE 알림을 테스트 합니다. 요청을 하게 되면 구독을 한 쪽으로 response가 생성됩니다. " +
            "\n\n 만약 구독이 끊겨도 event는 계속 생성되며, 유저가 구독한 이후에 끊긴 상태에서 생성된 event들에 대한 알림이 생성됩니다." +
            "                                           \n\n 현재 업적 1번 (첫 챌린지 달성)에 대한 알림만 보내게 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SSE 알림 전송 완료",
                    content = @Content(schema = @Schema(implementation = SseAchievementResponse.class)))
    })
    ResponseEntity<?> send(@AuthenticationPrincipal CustomUserDetails userDetails);

}

