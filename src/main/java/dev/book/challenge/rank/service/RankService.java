package dev.book.challenge.rank.service;

import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.challenge.ChallengeCategory;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.exception.ErrorCode;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.global.entity.Category;
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

        Challenge challenge = challengeRepository.findByIdJoinCategory(challengeId).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        List<Long> participantIds = userChallengeRepository.findUserIdByChallengeId(challengeId);
        List<ChallengeCategory> challengeCategories = challenge.getChallengeCategories();
        List<Category> categories = challengeCategories.stream().map(ChallengeCategory::getCategory).toList();

        return accountBookRepository.findByUserSpendingRanks(participantIds, categories, challenge.getStartDate().atStartOfDay(), challenge.getEndDate().atTime(23, 59, 59, 999_999_999));


    }
}