package dev.book.accountbook.scheduler;

import dev.book.accountbook.entity.Budget;
import dev.book.accountbook.repository.BudgetRepository;
import dev.book.accountbook.service.StatService;
import dev.book.achievement.achievement_user.IndividualAchievementStatusService;
import dev.book.achievement.achievement_user.dto.event.SaveConsumeFromBudgetEvent;
import dev.book.achievement.achievement_user.dto.event.SuccessBudgetPlanEvent;
import dev.book.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountBookScheduler {

    private final BudgetRepository budgetRepository;

    private final StatService statService;
    private final IndividualAchievementStatusService individualAchievementStatusService;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 0 1 * *") //매월 1일마다 예산 계획 성공 여부
    public void checkBudgetSuccessfulOrNot(){
        int lastMonth = LocalDate.now().minusMonths(1).getMonth().getValue();
        //저번 달 예산 계획을 세운 사람들
        List<Budget> budgets = budgetRepository.findAllByMonthWithUser(lastMonth);
        getLastMonthBudgetAndCheckSuccessUser(budgets);
        //저번 달 예산 계획을 세우지 않은 사람들
        List<UserEntity> userEntities = budgetRepository.findUsersWithoutBudgetAtMonth(lastMonth);
        userEntities.forEach(
                individualAchievementStatusService::didntHaveBudgetPlan
        );
    }

    private void getLastMonthBudgetAndCheckSuccessUser(List<Budget> budgets) {
        for (Budget budget : budgets){
            int lastMonthBudget = statService.getTotalConsumeOfLastMonth(budget.getUser().getId());
            if (lastMonthBudget < budget.getBudgetLimit()){
                // 저번 달 예산 계획과 비교하여 % 이상 절약한 사람들에게 업적 달성
                calcSavedRateAndAchieve(budget.getUser(), budget.getBudgetLimit(), lastMonthBudget);
                // 예산 계획에 성공한 사람들 횟수 +1
                eventPublisher.publishEvent(new SuccessBudgetPlanEvent(budget.getUser()));
            }
        }
    }

    private void calcSavedRateAndAchieve(UserEntity user, Integer thisAmount, Integer lastAmount) {
        if (lastAmount != null && lastAmount > 0) {
            double savedRate = ((double) (lastAmount - thisAmount) / lastAmount) * 100;
            if (savedRate >= 5.0)
                eventPublisher.publishEvent(new SaveConsumeFromBudgetEvent(user, savedRate));
        }
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void synchronizeAccount(){

    }

}
