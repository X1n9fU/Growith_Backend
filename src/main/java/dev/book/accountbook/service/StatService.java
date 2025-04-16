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
import dev.book.accountbook.type.Frequency;
import dev.book.accountbook.type.PeriodRange;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {
    private final AccountBookRepository accountBookRepository;

    public List<AccountBookStatResponse> statList(Long userId, Frequency frequency) {

        return getStatList(userId, frequency.calcStartDate());
    }

    public List<AccountBookSpendResponse> categoryList(Long userId, Frequency frequency, Category category) {

        return getCategoryList(userId, category, frequency.calcStartDate());
    }

    public AccountBookConsumeResponse consume(Long userId, Frequency frequency, Category category) {

        return getConsume(userId, category, frequency.calcPeriod());
    }

    private List<AccountBookStatResponse> getStatList(Long userId, LocalDateTime startDate) {
        List<AccountBookStatResponse> responses = accountBookRepository.findTop3Categories(userId, startDate, LocalDateTime.now(), PageRequest.of(0, 3));

        if (responses.isEmpty()) {
            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        }

        return responses;
    }

    private List<AccountBookSpendResponse> getCategoryList(Long userId, Category category, LocalDateTime starDate) {
        List<AccountBook> responses = accountBookRepository.findByCategory(userId, CategoryType.SPEND, category, starDate, LocalDateTime.now());

        if (responses.isEmpty()) {
            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_SPEND);
        }

        return responses.stream()
                .map(AccountBookSpendResponse::from)
                .toList();
    }

    private AccountBookConsumeResponse getConsume(Long userId, Category category, PeriodRange period) {
        Integer thisAmount = accountBookRepository.sumSpending(userId, CategoryType.SPEND, category, period.currentStart(), period.currentEnd());
        Integer lastAmount = accountBookRepository.sumSpending(userId, CategoryType.SPEND, category, period.previousStart(), period.previousEnd());

        return new AccountBookConsumeResponse(lastAmount - thisAmount);
    }
}
