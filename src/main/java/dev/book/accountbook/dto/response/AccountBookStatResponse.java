package dev.book.accountbook.dto.response;

import dev.book.accountbook.type.Category;

public record AccountBookStatResponse(Category category, Long sum) {
}
