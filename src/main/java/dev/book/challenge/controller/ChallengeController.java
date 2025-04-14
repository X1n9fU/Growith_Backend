package dev.book.challenge.controller;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/challenges")
    public ResponseEntity<?> createChallenge(@RequestBody ChallengeCreateRequest challengeCreateRequest) {
        ChallengeCreateResponse challengeCreateResponse = challengeService.createChallenge(challengeCreateRequest);
        return ResponseEntity.ok().body(challengeCreateResponse);
    }

}
