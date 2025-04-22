package dev.book.user.scheduler;

import dev.book.achievement.achievement_user.entity.IndividualAchievementStatus;
import dev.book.achievement.achievement_user.repository.IndividualAchievementStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final IndividualAchievementStatusRepository individualAchievementStatusRepository;

    /**
     * 매일 자정에 접속 했는지의 여부를 false로 되돌려 놓는다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void setEveryUserIsLoginFalse(){
        List<IndividualAchievementStatus> individualAchievementStatuses
                = individualAchievementStatusRepository.findAll();
        individualAchievementStatuses.forEach(
                ias -> {
                    if (ias.isLoginYesterday()) { //어제 로그인 했는데 오늘 로그인 x -> reset
                        if (!ias.isLoginToday()) {
                            ias.resetConsecutiveLogins();
                            ias.loginYesterday(false);
                        }  //어제 로그인, 오늘도 로그인 -> 연속
                    }
                    else { //어제 로그인 x, 오늘 로그인 -> 연속의 시작
                        if (ias.isLoginToday()) {
                            ias.loginYesterday(true);
                        } else { //어제 로그인 x, 오늘도 로그인 x -> reset
                            ias.resetConsecutiveLogins();
                        }
                    }
                    ias.loginToday(false);
                });

    }
}
