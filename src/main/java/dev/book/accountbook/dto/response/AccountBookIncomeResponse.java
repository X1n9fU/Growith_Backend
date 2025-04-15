package dev.book.accountbook.dto.response;

import dev.book.accountbook.dto.request.Repeat;
import dev.book.accountbook.entity.AccountBook;
import dev.book.accountbook.type.Category;

import java.time.LocalDateTime;

public record AccountBookIncomeResponse(Long id, String title, Category category, int amount, LocalDateTime updatedAt,
                                        String memo, LocalDateTime endDate, Repeat repeat) {
    public static AccountBookIncomeResponse from(AccountBook entity) {
        return new AccountBookIncomeResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getCategory(),
                entity.getAmount(),
                entity.getUpdatedAt(),
                entity.getMemo(),
                entity.getEndDate(),
                new Repeat(entity.getFrequency(), entity.getMonth(), entity.getDay())
        );
    }
}
