package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.Frequency;
import io.swagger.v3.oas.annotations.media.Schema;


public record Repeat(
        @Schema(description = "반복 주기", example = "monthly")
        Frequency frequency,

        @Schema(description = "년 단위 반복 시 월", example = "")
        Integer month,

        @Schema(description = "반복 날짜", example = "10")
        Integer day
) {}