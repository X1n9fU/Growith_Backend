package dev.book.accountbook.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetRequest(
        @Schema(description = "예산 금액", example = "1000000", minimum = "10")
        int budget
) {}
