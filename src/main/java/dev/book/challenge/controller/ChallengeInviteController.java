package dev.book.challenge.controller;

import dev.book.challenge.dto.request.ChallengeInviteRequest;
import dev.book.challenge.service.ChallengeInviteService;
import dev.book.global.config.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeInviteController {
    private final ChallengeInviteService challengeInviteService;

    @PostMapping("/{id}/invites")
    public ResponseEntity<String> invite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChallengeInviteRequest challengeInviteRequest) {
        challengeInviteService.invite(id, userDetails.user(), challengeInviteRequest);
        return ResponseEntity.ok().body("초대 완료 하였습니다");
    }
}
