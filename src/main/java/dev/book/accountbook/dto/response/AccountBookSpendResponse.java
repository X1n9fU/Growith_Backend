package dev.book.accountbook.dto.response;

import dev.book.accountbook.dto.request.Repeat;
import dev.book.accountbook.entity.AccountBook;

import java.time.LocalDateTime;
import java.util.List;

public record AccountBookSpendResponse(Long id, String title, List<String> category, int amount,
                                       LocalDateTime updatedAt, String memo, LocalDateTime endDate, Repeat repeat) {
    public static AccountBookSpendResponse from(AccountBook entity) {
        List<String> categoryNames = entity.getCategoryList().stream()
                .map(abCat -> abCat.getCategory().getCategory())
                .toList();


        return new AccountBookSpendResponse(
                entity.getId(),
                entity.getTitle(),
                categoryNames,
                entity.getAmount(),
                entity.getUpdatedAt(),
                entity.getMemo(),
                entity.getEndDate(),
                new Repeat(entity.getFrequency(), entity.getMonth(), entity.getDay())
        );
    }
}
