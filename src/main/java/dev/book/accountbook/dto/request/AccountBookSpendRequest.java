package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record AccountBookSpendRequest(
        @NotBlank
        @Size(max = 50)
        @Schema(description = "지출 이름", example = "핫도그")
        String title,

        @Schema(description = "카테고리", example = "[\"food\", \"shopping\"")
        List<String> categoryList,

        @NotNull
        @Min(10)
        @Max(100000000)
        @Schema(description = "지출 금액", example = "2500")
        int amount,

        @Size(max = 1000)
        @Schema(description = "메모", example = "밤에 배고파서 먹은 야식")
        String memo,

        @Schema(description = "정기 지출 종료일", example = "")
        LocalDateTime endDate,

        Repeat repeat
) implements AccountBookRequest {
    @Override
    public CategoryType categoryType() {
        return CategoryType.SPEND;
    }

    @Override
    public List<String> categoryList() {
        return categoryList;
    }
}