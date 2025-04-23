package dev.book.accountbook.service;

import dev.book.accountbook.dto.request.BudgetRequest;
import dev.book.accountbook.dto.response.BudgetResponse;
import dev.book.accountbook.entity.Budget;
import dev.book.accountbook.exception.accountbook.AccountBookErrorCode;
import dev.book.accountbook.exception.accountbook.AccountBookErrorException;
import dev.book.accountbook.repository.BudgetRepository;
import dev.book.achievement.achievement_user.dto.event.CreateBudgetEvent;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    private final ApplicationEventPublisher eventPublisher;

    public BudgetResponse getBudget(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));

        return budgetRepository.findBudgetWithTotal(user.getId());
    }

    @Transactional
    public BudgetResponse createBudget(UserEntity user, BudgetRequest budgetRequest) {
        int date = LocalDate.now().getMonthValue();
        budgetRepository.save(new Budget(budgetRequest.budget(), date, user));

        eventPublisher.publishEvent(new CreateBudgetEvent(user));
        return budgetRepository.findBudgetWithTotal(user.getId());
    }

    @Transactional
    public BudgetResponse modify(Long userId, Long id, BudgetRequest budgetRequest) {
        Budget budget = findBudgetIdAndUserId(id, userId);
        budget.modifyBudget(budgetRequest.budget());
        budgetRepository.flush();

        return budgetRepository.findBudgetWithTotal(userId);
    }

    @Transactional
    public void deleteBudget(Long userId, Long id) {
        Budget budget = findBudgetIdAndUserId(id, userId);
        budgetRepository.deleteById(budget.getId());
    }

    private Budget findBudgetIdAndUserId(Long budgetId, Long userId) {
        return budgetRepository.findByIdAndUserId(budgetId, userId).orElseThrow(() -> new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_BUDGET));
    }
}
