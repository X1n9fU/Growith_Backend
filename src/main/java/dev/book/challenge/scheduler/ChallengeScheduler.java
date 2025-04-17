package dev.book.challenge.scheduler;

import dev.book.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChallengeScheduler {
    private final ChallengeRepository challengeRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void closeChallenge() {
        LocalDate today = LocalDate.now();
        challengeRepository.updateChallengeStatusByDate(today);
    }
}
