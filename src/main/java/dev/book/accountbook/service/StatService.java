package dev.book.accountbook.service;

import dev.book.accountbook.dto.response.AccountBookConsumeResponse;
import dev.book.accountbook.dto.response.AccountBookSpendResponse;
import dev.book.accountbook.dto.response.AccountBookStatResponse;
import dev.book.accountbook.entity.AccountBook;
import dev.book.accountbook.exception.accountbook.AccountBookErrorCode;
import dev.book.accountbook.exception.accountbook.AccountBookErrorException;
import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.accountbook.type.Category;
import dev.book.accountbook.type.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {
    private final AccountBookRepository accountBookRepository;

    @Transactional(readOnly = true)
    public List<AccountBookStatResponse> statList(Long userId, String frequency) {
        return switch (frequency) {
            case "daily" -> getStatList(userId, calcYesterday());
            case "weekly" -> getStatList(userId, calcLastWeek());
            case "monthly" -> getStatList(userId, calcLastMonth());
            default -> throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        };

    }

    @Transactional(readOnly = true)
    public List<AccountBookSpendResponse> categoryList(Long userId, String frequency, Category category) {
        return switch (frequency) {
            case "daily" -> getCategoryList(userId, category, calcYesterday());
            case "weekly" -> getCategoryList(userId, category, calcLastWeek());
            case "monthly" -> getCategoryList(userId, category, calcLastMonth());
            default -> throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        };
    }

    @Transactional(readOnly = true)
    public AccountBookConsumeResponse consume(Long userId, String frequency, Category category) {
        return switch (frequency) {
            case "daily" -> getConsume(userId, category, calcYesterdayAllDay());
            case "weekly" -> getConsume(userId, category, calcLastWeekStartMonday());
            case "monthly" -> getConsume(userId, category, calcLastMonthStart());
            default -> throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        };
    }

    private List<AccountBookStatResponse> getStatList(Long userId, LocalDateTime endDate) {
        List<AccountBookStatResponse> responses = accountBookRepository.findTop3Categories(userId, endDate, LocalDateTime.now(), PageRequest.of(0, 3));

        if (responses.isEmpty()) {
            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        }

        return responses;
    }

    private List<AccountBookSpendResponse> getCategoryList(Long userId, Category category, LocalDateTime endDate) {
        List<AccountBook> responses = accountBookRepository.findByCategory(userId, CategoryType.SPEND, category, endDate, LocalDateTime.now());

        if (responses.isEmpty()) {
            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        }

        return responses.stream()
                .map(AccountBookSpendResponse::from)
                .toList();
    }

    private AccountBookConsumeResponse getConsume(Long userId, Category category, LocalDateTime[] endDate) {
        Integer thisAmount = accountBookRepository.sumSpending(userId, CategoryType.SPEND, category, endDate[0], endDate[1]);
        Integer lastAmount = accountBookRepository.sumSpending(userId, CategoryType.SPEND, category, endDate[2], endDate[3]);

        if (lastAmount == 0 || thisAmount == 0) {
            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        }

        return new AccountBookConsumeResponse(thisAmount - lastAmount);
    }

    private LocalDateTime calcYesterday() {
        return LocalDate.now().minusDays(1).atStartOfDay();
    }

    private LocalDateTime calcLastWeek() {
        return LocalDate.now().minusWeeks(1).atStartOfDay();
    }

    private LocalDateTime calcLastMonth() {
        return LocalDate.now().minusMonths(1).atStartOfDay();
    }

    private LocalDateTime[] calcYesterdayAllDay() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime yesterdayStart = yesterday.atStartOfDay();
        LocalDateTime yesterdayEnd = yesterday.atTime(LocalTime.MAX);

        return new LocalDateTime[]{todayStart, now, yesterdayStart, yesterdayEnd};
    }

    private LocalDateTime[] calcLastWeekStartMonday() {
        LocalDate today = LocalDate.now();
        LocalDate thisWeekStart = today.with(DayOfWeek.MONDAY);
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate lastWeekEnd = thisWeekStart.minusDays(1);

        LocalDateTime lastWeekStartDateTime = lastWeekStart.atStartOfDay();
        LocalDateTime lastWeekEndDateTime = lastWeekEnd.atTime(LocalTime.MAX);

        return new LocalDateTime[]{thisWeekStart.atStartOfDay(), LocalDateTime.now(), lastWeekStartDateTime, lastWeekEndDateTime};
    }

    private LocalDateTime[] calcLastMonthStart() {
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
}
