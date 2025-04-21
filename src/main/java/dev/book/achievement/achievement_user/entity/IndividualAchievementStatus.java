package dev.book.achievement.achievement_user.entity;

import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 업적 달성을 파악하기 위한 개인별 현황도
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualAchievementStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    private int completeChallenge=0; //챌린지 달성
    private int failChallenge=0; //챌린지 실패
    private int createChallenge=0; //챌린지 생성
    private boolean isLoginToday=false; //오늘 로그인 했는지

    private long consecutiveLogins=0; //연속적으로 로그인한 횟수

    private long checkSpendAnalysis=0; //소비 분석 탭을 확인한 횟수

    private boolean createFirstIncome=false;//첫번째 고정 수입 등록

    private int consecutiveNoSpend=0; //연속적으로 무지출한 횟수

    private int createBudget=0; //예산 계획 작성 횟수

    private int successBudgetPlan=0; //예산 계획 지킨 횟수

    private int getWarningBudget=0; //과소비 경고 받은 횟수

    private int inviteFriendToService=0; //서비스 내에 친구를 초대한 횟수

    private int inviteFriendToChallenge=0; //챌린지에 친구를 초대한 횟수

    private int shareTips=0;//소비 팁을 공유한 횟수

    public IndividualAchievementStatus(UserEntity user) {
        this.user = user;
    }
    public void plusCompleteChallenge(){
        this.completeChallenge++;
    }

    public void plusFailChallenge(){
        this.failChallenge++;
    }
    public void plusCreateChallenge(){
        this.createChallenge++;
    }
    public void loginToday(boolean isLoginToday){
        this.isLoginToday=isLoginToday;
    }

    public void plusConsecutiveLogins(){
        this.consecutiveLogins++;
    }

    public void pluCheckSpendAnalysis(){
        this.checkSpendAnalysis++;
    } //소비 분석 탭을 확인한 횟수

    public void plusCreateFirstIncome(){
        this.createFirstIncome = true;
    }

    public void plusConsecutiveNoSpend(){
        this.consecutiveNoSpend++;
    }

    public void plusCreateBudget(){
        this.createBudget++;
    }

    public void plusSuccessBudgetPlan(){
        this.successBudgetPlan++;
    }

    public void plusGetWarningBudget(){
        this.getWarningBudget++;
    }

    public void plusInviteFriendToService(){
        this.inviteFriendToService++;
    }

    public void plusInviteFriendToChallenge(){
        this.inviteFriendToChallenge++;
    }

    public void plusShareTips(){
        this.shareTips++;
    }


}
