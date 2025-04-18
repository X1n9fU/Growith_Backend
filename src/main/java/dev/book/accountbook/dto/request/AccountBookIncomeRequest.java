package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.Category;
import dev.book.accountbook.type.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record AccountBookIncomeRequest(

        @NotNull
        @Size(max = 50)
        @Schema(description = "수입 제목", example = "월급")
        String title,

        @Schema(description = "카테고리", example = "salary")
        Category category,

        @NotNull
        @Min(10)
        @Max(100000000)
        @Schema(description = "수입 금액", example = "3000000")
        int amount,

        @Size(max = 1000)
        @Schema(description = "수입 메모", example = "4월 정기 급여")
        String memo,

        @Schema(description = "수입 종료일 (반복 수입일 경우)", example = "2025-12-31T23:59:59")
        LocalDateTime endDate,

        Repeat repeat

) implements AccountBookRequest {

    @Override
    public CategoryType categoryType() {
        return CategoryType.INCOME;
    }
}