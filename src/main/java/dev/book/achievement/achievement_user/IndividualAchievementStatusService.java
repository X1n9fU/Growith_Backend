package dev.book.achievement.achievement_user;

import dev.book.achievement.achievement_user.entity.IndividualAchievementStatus;
import dev.book.achievement.achievement_user.repository.IndividualAchievementStatusRepository;
import dev.book.achievement.exception.AchievementErrorCode;
import dev.book.achievement.exception.AchievementException;
import dev.book.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndividualAchievementStatusService {

    private final IndividualAchievementStatusRepository individualAchievementStatusRepository;
    public void plusCompleteChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusCompleteChallenge();
    }

    public void plusFailChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusFailChallenge();
    }
    public void plusCreateChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusCreateChallenge();
    }
    public void loginToday(UserEntity user, boolean isLoginToday){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.loginToday(isLoginToday);
    }

    public void plusConsecutiveLogins(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusConsecutiveLogins();
    }

    public void pluCheckSpendAnalysis(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.pluCheckSpendAnalysis();
    }

    public void plusCreateFirstIncome(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusCreateFirstIncome();
    }

    public void plusConsecutiveNoSpend(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusConsecutiveNoSpend();
    }

    public void plusCreateBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusCreateBudget();
    }

    public void plusSuccessBudgetPlan(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusSuccessBudgetPlan();
    }

    public void plusGetWarningBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusGetWarningBudget();
    }

    public void plusInviteFriendToService(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusInviteFriendToService();
    }

    public void plusInviteFriendToChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusInviteFriendToChallenge();
    }

    public void plusShareTips(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusShareTips();
    }

    private IndividualAchievementStatus getIndividualAchievementStatus(UserEntity user){
        return individualAchievementStatusRepository.findByUser(user)
                .orElseThrow(() -> new AchievementException(AchievementErrorCode.INDIVIDUAL_ACHIEVEMENT_NOT_FOUND));
    }
}
