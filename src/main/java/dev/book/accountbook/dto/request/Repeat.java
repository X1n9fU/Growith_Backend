package dev.book.accountbook.dto.request;

import dev.book.accountbook.type.Frequency;

public record Repeat(Frequency frequency, Integer month, Integer day) {
}
