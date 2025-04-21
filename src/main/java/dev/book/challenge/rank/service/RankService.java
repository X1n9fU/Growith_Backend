package dev.book.challenge.rank.service;

import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.challenge.ChallengeCategory;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.global.entity.Category;
import dev.book.global.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AccountBookRepository accountBookRepository;
    private final CategoryRepository categoryRepository;

    public List<RankResponse> checkRank(Long challengeId) {

//어떤 챌린지 찾고 그 챌린지에 아이디들을 조회
        Challenge challenge = challengeRepository.findByIdA(challengeId).orElseThrow();
        List<Long> participantIds = userChallengeRepository.findUserIdByChallengeId(challengeId);
        List<ChallengeCategory> challengeCategories = challenge.getChallengeCategories();
        List<Category> categories = challengeCategories.stream().map(ChallengeCategory::getCategory).toList();

        return accountBookRepository.findByUserSpendingRanks(participantIds, categories, challenge.getStartDate().atStartOfDay(), challenge.getEndDate().atStartOfDay().minusNanos(1));


    }
}
// 챌린지를 찾고 그 챌린지에 있는 아이디들을 조회 챌린지에카테고리도 알아야해