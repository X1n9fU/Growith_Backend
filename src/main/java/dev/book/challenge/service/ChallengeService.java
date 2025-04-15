package dev.book.challenge.service;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.dto.response.ChallengeUpdateResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.exception.ErrorCode;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user_challenge.entity.UserChallenge;
import dev.book.user_challenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    public ChallengeCreateResponse createChallenge(UserEntity user, ChallengeCreateRequest challengeCreateRequest) {

        Challenge challenge = Challenge.of(challengeCreateRequest, user);
        Challenge savedChallenge = challengeRepository.save(challenge);
        UserChallenge userChallenge = UserChallenge.of(user, savedChallenge);
        userChallengeRepository.save(userChallenge);
        return ChallengeCreateResponse.fromEntity(savedChallenge);
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
        Challenge challenge = getMyChallenge(user, id);
        challenge.updateInfo(challengeUpdateRequest);
        return ChallengeUpdateResponse.fromEntity(challenge);
    }


    public void deleteChallenge(UserEntity user, Long id) {
        Challenge challenge = getMyChallenge(user, id);
        challengeRepository.delete(challenge);

    }

    private Challenge getMyChallenge(UserEntity user, Long id) {
        Challenge challenge = challengeRepository.findByIdAndCreator(id, user).orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_INVALID));
        return challenge;
    }
}
