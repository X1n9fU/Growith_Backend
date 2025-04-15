package dev.book.accountbook.dto.response;

import dev.book.accountbook.type.Category;

import java.time.LocalDateTime;

public record AccountBookCategoryResponse(String title, int amount, Category category, LocalDateTime modifyDate) {
}
