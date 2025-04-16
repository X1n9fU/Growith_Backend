package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.Category;
import dev.book.accountbook.type.CategoryType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record AccountBookIncomeRequest(@NotNull @Size(max = 50) String title, Category category, @NotNull @Min(10) @Max(100000000) int amount, @Size(max = 1000) String memo, LocalDateTime endDate, Repeat repeat
) implements AccountBookRequest {

    @Override
    public CategoryType categoryType() {
        return CategoryType.INCOME;
    }
}
