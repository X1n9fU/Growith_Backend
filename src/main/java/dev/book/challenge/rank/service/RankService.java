package dev.book.challenge.rank.service;

import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.global.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.book.challenge.exception.ErrorCode.CHALLENGE_NOT_FOUND_CATEGORY;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AccountBookRepository accountBookRepository;
    private final CategoryRepository categoryRepository;

    public List<RankResponse> checkRank(Long challengeId) {


        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow();
        List<Long> participantIds = userChallengeRepository.findUserIdByChallengeId(challengeId);
        Category challengecategory = challenge.getChallengeCategory();

        dev.book.global.entity.Category categoryEntity =
                categoryRepository.findByCategory(challengecategory.name())
                        .orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND_CATEGORY));
        return accountBookRepository.findByUserSpendingRanks(participantIds, categoryEntity, challenge.getStartDate().atStartOfDay(), challenge.getEndDate().atStartOfDay().minusNanos(1));


    }
}
