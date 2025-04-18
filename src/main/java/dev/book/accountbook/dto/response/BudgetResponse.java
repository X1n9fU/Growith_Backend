package dev.book.accountbook.dto.response;

import dev.book.accountbook.entity.Budget;

public record BudgetResponse(int budget, long total) {
    public static BudgetResponse from(Budget budget, int total) {
        return new BudgetResponse(
                budget.getBudgetLimit(), total
        );
    }
}
