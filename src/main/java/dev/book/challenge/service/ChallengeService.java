package dev.book.challenge.service;

import dev.book.achievement.achievement_user.IndividualAchievementStatusService;
import dev.book.challenge.ChallengeCategory;
import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.*;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.exception.ErrorCode;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.entity.UserChallenge;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.global.entity.Category;
import dev.book.global.repository.CategoryRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static dev.book.challenge.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final IndividualAchievementStatusService individualAchievementStatusService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChallengeCreateResponse createChallenge(UserEntity user, ChallengeCreateRequest challengeCreateRequest) {

        UserEntity creator = userRepository.findByEmail(user.getEmail()).orElseThrow();
        List<Category> categories = categoryRepository.findByCategoryIn(challengeCreateRequest.categoryList());
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        categories.forEach(category -> new ChallengeCategory(challenge, category));
        Challenge savedChallenge = challengeRepository.save(challenge);
        UserChallenge userChallenge = UserChallenge.of(creator, savedChallenge);
        userChallengeRepository.save(userChallenge);
        individualAchievementStatusService.plusCreateChallenge(user);
        creator.plusChallengeCount();
        return ChallengeCreateResponse.fromEntity(challenge);

    }

    public Page<ChallengeReadResponse> searchChallenge(String title, String text, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        return challengeRepository.search(title, text, pageable);
    }

    public ChallengeReadDetailResponse searchChallengeById(Long id) {

        Challenge challenge = challengeRepository.findWithCreatorById(id).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        return ChallengeReadDetailResponse.fromEntity(challenge);
    }

    @Transactional
    public ChallengeUpdateResponse updateChallenge(UserEntity user, Long id, ChallengeUpdateRequest challengeUpdateRequest) {

        Challenge challenge = getMyChallenge(user.getId(), id);
        List<Category> categories = categoryRepository.findByCategoryIn(challengeUpdateRequest.categoryList());
        challenge.updateInfo(challengeUpdateRequest, categories);
        challengeRepository.flush();
        return ChallengeUpdateResponse.fromEntity(challenge, categories);
    }

    @Transactional
    public void deleteChallenge(UserEntity user, Long id) {

        UserEntity creator = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        Challenge challenge = getMyChallenge(creator.getId(), id);
        List<Long> userIds = userChallengeRepository.findUserIdByChallengeId(challenge.getId());
        List<UserEntity> users = userRepository.findAllById(userIds);

        for (UserEntity participant : users) {
            participant.minusChallengeCount();
        } // 만약에 챌린지가 완료후 챌린지가 삭제 되면 이것도 삭제되는데

        challengeRepository.delete(challenge);

    }

    @Transactional
    public void participate(UserEntity user, Long id) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        Challenge challenge = challengeRepository.findByIdWithRock(id).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        challenge.checkAlreadyStartOrEnd();
        checkExist(user, id);
        challenge.isOver();
        challenge.plusCurrentCapacity();
        userEntity.plusChallengeCount();
        UserChallenge userChallenge = UserChallenge.of(userEntity, challenge);
        userChallengeRepository.save(userChallenge);
    }


    @Transactional
    public void leaveChallenge(UserEntity user, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND));
        UserEntity userEntity = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND));
        checkNotExist(user, challenge.getId());
        userEntity.minusChallengeCount();
        challenge.minusCurrentCapacity();
        userChallengeRepository.deleteByUserIdAndChallengeId(userEntity.getId(), challenge.getId());

    }

    public List<ChallengeTopResponse> findTopChallenge() {

        Pageable pageable = PageRequest.of(0, 3); //todo top 갯수 추가 조정
        return challengeRepository.findTopChallenge(pageable);

    }

    private void checkExist(UserEntity user, Long id) {

        boolean isExist = userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), id);

        if (isExist) {
            throw new ChallengeException(CHALLENGE_ALREADY_JOINED);
        }

    }

    private void checkNotExist(UserEntity user, Long challengeId) {
        boolean isNotExist = !userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId);

        if (isNotExist) {
            throw new ChallengeException(CHALLENGE_NOT_FOUND_USER);
        }
    }

    private Challenge getMyChallenge(Long userId, Long id) {

        return challengeRepository.findByIdAndCreatorId(id, userId).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_INVALID));
    }

    public List<ChallengeReadResponse> findNewChallenge() {

        Pageable pageable = PageRequest.of(0, 3); //todo new  갯수 추가 조정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startToday = now.toLocalDate().atStartOfDay();
        LocalDateTime endToday = now.toLocalDate().atTime(LocalTime.MAX);
        return challengeRepository.findNewChallenge(pageable, startToday, endToday);
    }
}
