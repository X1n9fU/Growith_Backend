package dev.book.challenge;

import dev.book.accountbook.entity.AccountBook;
import dev.book.accountbook.entity.middle.AccountBookCategory;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.rank.SpendCreatedRankingEvent;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.rank.service.RankService;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SpendEventListener {
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final RankService rankService;
    private final SimpMessagingTemplate messagingTemplate;


    @EventListener
    public void handleSpendCreatedEvent(SpendCreatedRankingEvent event) {

        AccountBook accountBook = event.accountBook();
        Long userId = accountBook.getUser().getId();
        List<AccountBookCategory> accountBookCategories = event.accountBook().getCategoryList();


        List<Challenge> challenges = challengeRepository.findAllByCategoryAndDate(accountBook.getEndDate().toLocalDate());

        List<Challenge> relatedChallenges = challenges.stream()
                .filter(c -> {
                    List<Long> participantIds = userChallengeRepository.findUserIdByChallengeId(c.getId());
                    return participantIds.contains(userId);
                })
                .filter(c ->c.getChallengeCategories().contains(accountBookCategories))
                .toList();

        for (Challenge challenge : relatedChallenges) {
            List<RankResponse> ranks = rankService.checkRank(challenge.getId());
            messagingTemplate.convertAndSend("/sub/challenge/" + challenge.getId() + "/rank", ranks);
        }

    }
}
