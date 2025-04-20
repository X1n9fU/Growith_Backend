package dev.book.accountbook.entity;

import dev.book.accountbook.entity.middle.BudgetAccountBook;
import dev.book.accountbook.entity.middle.BudgetCategory;
import dev.book.global.entity.BaseTimeEntity;
import dev.book.global.entity.Category;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int budgetLimit;
    private int month;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetAccountBook> budgetReferences = new ArrayList<>();

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> budgetCategories = new ArrayList<>();

    public void addCategory(Category category) {
        BudgetCategory bc = new BudgetCategory(this, category);
        this.budgetCategories.add(bc);
        category.getBudgets().add(bc); // 양방향이면 필요
    }

    public Budget(int budgetLimit, int month, UserEntity user) {
        this.budgetLimit = budgetLimit;
        this.month = month;
        this.user = user;
    }

    public void modifyBudget(int budgetLimit) {
        int date = LocalDate.now().getMonthValue();

        this.budgetLimit = budgetLimit;
        this.month = date;
    }
}
