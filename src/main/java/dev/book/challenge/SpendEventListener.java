package dev.book.challenge;

import dev.book.accountbook.entity.AccountBook;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.rank.SpendCreatedRankingEvent;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.rank.service.RankService;
import dev.book.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SpendEventListener {
    private final ChallengeRepository challengeRepository;
    private final RankService rankingService;
    private final SimpMessagingTemplate messagingTemplate;


    @EventListener
    public void handleSpendCreatedEvent(SpendCreatedRankingEvent event) {

        AccountBook ab = event.accountBook();
        LocalDateTime date = ab.getEndDate();
        String userEmail = ab.getUser().getEmail();

        List<Challenge> challenges = challengeRepository.findAllByCategoryAndDate(date);

        List<Challenge> relatedChallenges = challenges.stream()
                .filter(c -> c.getChallengeCategory()
                        .getRelatedSpendingCategories()
                        .contains(ab.getCategory()))
                .toList();

        for (Challenge challenge : relatedChallenges) {
            List<RankResponse> ranks = rankingService.checkRank(challenge.getId(), userEmail);
            messagingTemplate.convertAndSend("/sub/rank/challenge/" + challenge.getId(), ranks);
        }

    }
}
