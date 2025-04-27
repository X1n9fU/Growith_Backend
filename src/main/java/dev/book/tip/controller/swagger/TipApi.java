package dev.book.tip.controller.swagger;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.tip.dto.request.TipRequest;
import dev.book.tip.dto.response.TipResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "팁 API", description = "팁 작성, 조회 api")
public interface TipApi {

    @Operation(summary = "팁 작성 API", description = "팁을 새로 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팁 작성 성공",
                    content = @Content(schema = @Schema(implementation = TipResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 해당 챌린지에 팁을 작성하였습니다.")
    })
    ResponseEntity<?> createTip(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TipRequest tipRequest);

    @Operation(summary = "팁 반환 API", description = "실시간으로 받은 팁이 소실되었을 경우, 사전에 저장되었던 팁 중 랜덤 10개를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팁 반환 성공")
    })
    ResponseEntity<?> getTips();

}
