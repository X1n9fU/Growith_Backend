package dev.book.challenge.scheduler;

import dev.book.challenge.entity.Challenge;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChallengeScheduler {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void closeChallenge() {
        LocalDate today = LocalDate.now();
        List<Challenge> challenges = challengeRepository.findChallengesToUpdate(today);
        for (Challenge challenge : challenges){
            challenge.completeChallenge();
            List<Long> userIds = userChallengeRepository.findUserIdByChallengeId(challenge.getId());
            //챌린지의 기간 동안의 내역을 가져와서 챌린지의 한도 내역과 비교

        }
    }
}
