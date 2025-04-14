package dev.book.challenge.controller;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/challenges")
    public ResponseEntity<?> createChallenge(@RequestBody ChallengeCreateRequest challengeCreateRequest) {
        ChallengeCreateResponse challengeCreateResponse = challengeService.createChallenge(challengeCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeCreateResponse);
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeReadResponse>> searchChallenge() {
        List<ChallengeReadResponse> challengeReadResponses = challengeService.searchChallenge();
        return ResponseEntity.ok().body(challengeReadResponses);
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeReadDetailResponse> searchById(@PathVariable Long id) {
        ChallengeReadDetailResponse challengeReadResponse = challengeService.searchChallengeById(id);
        return ResponseEntity.ok().body(challengeReadResponse);
    }

}
