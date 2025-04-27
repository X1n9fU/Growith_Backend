package dev.book.tip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record TipRequest (

        @Schema(description = "Tip을 작성하는 챌린지 id(pk)", defaultValue = "1")
        Long challengeId,

        @Schema(description = "Tip 내용", defaultValue = "가끔은 교통수단도 좋아요!")
        String content) {
}
