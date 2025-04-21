package dev.book.accountbook.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AccountBookCategoryResponse(String title, int amount, List<String> categories, LocalDateTime modifyDate) {
}
