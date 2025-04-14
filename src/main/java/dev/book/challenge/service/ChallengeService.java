package dev.book.challenge.service;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //todo 필터링 및 검색 기능 추가,페이징
    public List<ChallengeReadResponse> searchChallenge() {
        List<Challenge> challenges = challengeRepository.findAll();
        List<ChallengeReadResponse> challengeReadResponses = challenges.stream().map(ChallengeReadResponse::fromEntity).toList();
        return challengeReadResponses;
    }

    public ChallengeReadDetailResponse searchChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        return ChallengeReadDetailResponse.fromEntity(challenge);
    }
}
