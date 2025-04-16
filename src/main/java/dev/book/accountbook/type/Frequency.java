package dev.book.accountbook.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum Frequency {
    DAILY {
        @Override
        public LocalDateTime calcStartDate() {
            return LocalDate.now().minusDays(1).atStartOfDay();
        }

        @Override
        public LocalDateTime[] calcPeriod() {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            LocalDateTime todayStart = today.atStartOfDay();
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime yesterdayStart = yesterday.atStartOfDay();
            LocalDateTime yesterdayEnd = yesterday.atTime(LocalTime.MAX);

            return new LocalDateTime[]{todayStart, now, yesterdayStart, yesterdayEnd};
        }
    },
    WEEKLY {
        @Override
        public LocalDateTime calcStartDate() {
            return LocalDate.now().minusWeeks(1).atStartOfDay();
        }

        @Override
        public LocalDateTime[] calcPeriod() {
            LocalDate today = LocalDate.now();
            LocalDate thisWeekStart = today.with(DayOfWeek.MONDAY);
            LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
            LocalDate lastWeekEnd = thisWeekStart.minusDays(1);

            LocalDateTime lastWeekStartDateTime = lastWeekStart.atStartOfDay();
            LocalDateTime lastWeekEndDateTime = lastWeekEnd.atTime(LocalTime.MAX);

            return new LocalDateTime[]{thisWeekStart.atStartOfDay(), LocalDateTime.now(), lastWeekStartDateTime, lastWeekEndDateTime};
        }
    },
    MONTHLY {
        @Override
        public LocalDateTime calcStartDate() {
            return LocalDate.now().minusMonths(1).atStartOfDay();
        }

        @Override
        public LocalDateTime[] calcPeriod() {
            LocalDate today = LocalDate.now();
            LocalDate thisMonthStart = today.withDayOfMonth(1);
            LocalDate lastMonthStart = thisMonthStart.minusMonths(1);
            LocalDate lastMonthEnd = thisMonthStart.minusDays(1);

            LocalDateTime thisMonthStartDateTime = thisMonthStart.atStartOfDay();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastMonthStartDateTime = lastMonthStart.atStartOfDay();
            LocalDateTime lastMonthEndDateTime = lastMonthEnd.atTime(LocalTime.MAX);

            return new LocalDateTime[]{thisMonthStartDateTime, now, lastMonthStartDateTime, lastMonthEndDateTime};
        }
    };

    @JsonCreator
    public static Frequency from(String value) {
        return Frequency.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }

    public abstract LocalDateTime calcStartDate();

    public abstract LocalDateTime[] calcPeriod();
}
