package dev.book.accountbook.repository;

import dev.book.accountbook.dto.response.BudgetResponse;

public interface BudgetRepositoryCustom {
    BudgetResponse findBudgetWithTotal(Long userId);
}
