package dev.book.challenge.service;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.dto.response.ChallengeUpdateResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.repository.ChallengeRepository;
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

    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest challengeCreateRequest) {
        // todo 유저 생성 로직 추가
        Challenge challenge = Challenge.of(challengeCreateRequest);
        Challenge savedChallenge = challengeRepository.save(challenge);
        return ChallengeCreateResponse.fromEntity(savedChallenge);
    }

    public Page<ChallengeReadResponse> searchChallenge(String title, String text, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return challengeRepository.search(title, text, pageable);
    }

    public ChallengeReadDetailResponse searchChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        return ChallengeReadDetailResponse.fromEntity(challenge);
    }

    @Transactional
    public ChallengeUpdateResponse updateChallenge(Long id, ChallengeUpdateRequest challengeUpdateRequest) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        challenge.updateInfo(challengeUpdateRequest);
        challengeRepository.flush();
        return ChallengeUpdateResponse.fromEntity(challenge);
    }
}
