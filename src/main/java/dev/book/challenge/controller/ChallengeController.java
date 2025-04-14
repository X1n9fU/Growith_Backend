package dev.book.challenge.controller;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.dto.response.ChallengeUpdateResponse;
import dev.book.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ChallengeReadResponse>> searchChallenge(@RequestParam(required = false) String title,
                                                                       @RequestParam(required = false) String text,
                                                                       @RequestParam(required = false, defaultValue = "1") int page,
                                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        Page<ChallengeReadResponse> challengeReadResponses = challengeService.searchChallenge(title, text, page, size);
        return ResponseEntity.ok().body(challengeReadResponses);
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeReadDetailResponse> searchById(@PathVariable Long id) {
        ChallengeReadDetailResponse challengeReadResponse = challengeService.searchChallengeById(id);
        return ResponseEntity.ok().body(challengeReadResponse);
    }

    @PutMapping("/challenges/{id}")
    public ResponseEntity<?> updateChallenge(@PathVariable Long id, @RequestBody ChallengeUpdateRequest challengeUpdateRequest) {
        ChallengeUpdateResponse challengeUpdateResponse = challengeService.updateChallenge(id, challengeUpdateRequest);
        return ResponseEntity.ok().body(challengeUpdateResponse);
    }

    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.ok().build();
    }


}
