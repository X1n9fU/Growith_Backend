package dev.book.accountbook.type;

import java.time.LocalDateTime;

public record PeriodRange(LocalDateTime currentStart, LocalDateTime currentEnd, LocalDateTime previousStart, LocalDateTime previousEnd) {
}
