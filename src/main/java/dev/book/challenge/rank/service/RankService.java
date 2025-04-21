package dev.book.challenge.rank.service;

import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.accountbook.type.Category;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AccountBookRepository accountBookRepository;

    public List<RankResponse> checkRank(Long challengeId) {


        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow();
        List<Long> participantIds = userChallengeRepository.findUserIdByChallengeId(challengeId);
        List<Category> categories = challenge.getChallengeCategory().getRelatedSpendingCategories();

        return accountBookRepository.findByUserSpendingRanks(participantIds, categories, challenge.getStartDate().atStartOfDay(), challenge.getEndDate().atStartOfDay().minusNanos(1));


    }
}
