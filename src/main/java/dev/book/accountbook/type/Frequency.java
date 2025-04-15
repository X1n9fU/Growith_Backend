package dev.book.accountbook.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Frequency {
    WEEKLY, MONTHLY, YEARLY;

    @JsonCreator
    public static Frequency from(String value) {
        return Frequency.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }
}
