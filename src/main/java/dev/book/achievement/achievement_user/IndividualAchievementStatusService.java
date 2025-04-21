package dev.book.achievement.achievement_user;

import dev.book.achievement.achievement_user.entity.IndividualAchievementStatus;
import dev.book.achievement.achievement_user.repository.IndividualAchievementStatusRepository;
import dev.book.achievement.service.AchievementService;
import dev.book.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndividualAchievementStatusService {

    private final IndividualAchievementStatusRepository individualAchievementStatusRepository;
    private final AchievementService achievementService;

    @Transactional
    public void deterMineContinuous(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        long continuous = plusConsecutiveLogins(achievementStatus);
        if (continuous > 0)
            saveConsecutiveLoginAchievement(continuous, user.getId());
    }

    public void saveConsecutiveLoginAchievement(long consecutiveLogins, long userId){
        if (consecutiveLogins == 7L)
            achievementService.saveAchievement(7L, userId);
        if (consecutiveLogins == 30L)
            achievementService.saveAchievement(8L, userId);
    }

    private static long plusConsecutiveLogins(IndividualAchievementStatus achievementStatus) {
        boolean isLoginToday = achievementStatus.isLoginToday();
        long continuous;
        if (!isLoginToday){
            continuous = achievementStatus.plusConsecutiveLogins();
            achievementStatus.loginToday(true);
        } else {
            continuous = -1;
        }
        return continuous;
    }

    public void plusCompleteChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int completeChallenge = achievementStatus.plusCompleteChallenge();
        switch(completeChallenge){
            case 1:
                achievementService.saveAchievement(1L, user.getId());
            case 5:
                achievementService.saveAchievement(2L, user.getId());
            case 10:
                achievementService.saveAchievement(3L, user.getId());
        }

    }

    public void plusFailChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int failChallenge = achievementStatus.plusFailChallenge();
        switch(failChallenge){
            case 1:
                achievementService.saveAchievement(4L, user.getId());
        }
    }

    public void plusCreateChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int createChallenge = achievementStatus.plusCreateChallenge();
        switch (createChallenge){
            case 1:
                achievementService.saveAchievement(5L, user.getId());
            case 5:
                achievementService.saveAchievement(6L, user.getId());
        }
    }
    public void loginToday(UserEntity user, boolean isLoginToday){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.loginToday(isLoginToday);
    }

    public void resetConsecutiveLogins(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.resetConsecutiveLogins();
    }

    public void pluCheckSpendAnalysis(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        long checkSpendAnalysis = achievementStatus.pluCheckSpendAnalysis();
        if (checkSpendAnalysis == 1L)
            achievementService.saveAchievement(9L, user.getId());
    }

    public void plusCreateFirstIncome(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        achievementStatus.plusCreateFirstIncome();
        achievementService.saveAchievement(10L, user.getId());
    }

    public void saveFivePercentOnLastWeek(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        if (!achievementStatus.isSaveFivePercentOnLastWeek()){
            achievementStatus.setSaveFivePercentOnLastWeek();
            achievementService.saveAchievement(11L, user.getId());
        }
    }

    public void saveTenPercentOnLastWeek(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        if (!achievementStatus.isSaveTenPercentOnLastWeek()){
            achievementStatus.setSaveTenPercentOnLastWeek();
            achievementService.saveAchievement(12L, user.getId());
        }
    }

    public void plusConsecutiveNoSpend(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int consecutiveNoSpend = achievementStatus.plusConsecutiveNoSpend();
        switch (consecutiveNoSpend){
            case 3:
                achievementService.saveAchievement(13L, user.getId());
        }
    }

    public void plusCreateBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int createBudget = achievementStatus.plusCreateBudget();
        switch(createBudget){
            case 1:
                achievementService.saveAchievement(14L, user.getId());
            case 3:
                achievementService.saveAchievement(15L, user.getId());
        }
    }

    public void plusSuccessBudgetPlan(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int successBudgetPlan = achievementStatus.plusSuccessBudgetPlan();
        switch(successBudgetPlan){
            case 1:
                achievementService.saveAchievement(16L, user.getId());
            case 3:
                achievementService.saveAchievement(17L, user.getId());
        }
    }

    public void plusGetWarningBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int getWarningBudget = achievementStatus.plusGetWarningBudget();
        if (getWarningBudget==1) achievementService.saveAchievement(18L, user.getId());
    }

    public void saveFivePercentFromBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        if (!achievementStatus.isSaveFivePercentFromBudget()){
            achievementStatus.setSaveFivePercentFromBudget();
            achievementService.saveAchievement(19L, user.getId());
        }
    }

    public void saveTenPercentFromBudget(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        if (!achievementStatus.isSaveTenPercentFromBudget()){
            achievementStatus.setSaveTenPercentFromBudget();
            achievementService.saveAchievement(20L, user.getId());
        }
    }


    public void plusInviteFriendToService(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int inviteFriendToService = achievementStatus.plusInviteFriendToService();
        switch (inviteFriendToService){
            case 1:
                achievementService.saveAchievement(21L, user.getId());
            case 3:
                achievementService.saveAchievement(22L, user.getId());
        }
    }

    public void plusInviteFriendToChallenge(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int inviteFriendToChallenge = achievementStatus.plusInviteFriendToChallenge();
        switch (inviteFriendToChallenge){
            case 1:
                achievementService.saveAchievement(23L, user.getId());
        }
    }

    public void plusShareTips(UserEntity user){
        IndividualAchievementStatus achievementStatus = getIndividualAchievementStatus(user);
        int shareTips = achievementStatus.plusShareTips();
        switch (shareTips){
            case 1:
                achievementService.saveAchievement(24L, user.getId());
            case 5:
                achievementService.saveAchievement(25L, user.getId());
        }
    }

    private IndividualAchievementStatus getIndividualAchievementStatus(UserEntity user){
        return individualAchievementStatusRepository.findByUser(user)
                .orElse(individualAchievementStatusRepository.save(new IndividualAchievementStatus(user)));
    }
}
