package dev.book.challenge.service;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
