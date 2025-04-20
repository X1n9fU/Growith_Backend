package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record AccountBookIncomeRequest(

        @NotNull
        @Size(max = 50)
        @Schema(description = "수입 제목", example = "월급")
        String title,

        @Schema(description = "카테고리 목록", example = "[\"salary\", \"bonus\"]")
        List<String> categoryList,

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

    @Override
    public List<String> categoryList() {
        return categoryList;
    }
}