package dev.book.challenge.controller;

import dev.book.challenge.api.ChallengeInviteApi;
import dev.book.challenge.dto.request.ChallengeInviteRequest;
import dev.book.challenge.dto.response.ChallengeInviteResponse;
import dev.book.challenge.service.ChallengeInviteService;
import dev.book.global.config.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeInviteController implements ChallengeInviteApi {

    private final ChallengeInviteService challengeInviteService;

    @PostMapping("/{id}/invites")
    public ResponseEntity<String> invite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChallengeInviteRequest challengeInviteRequest) {
        challengeInviteService.invite(id, userDetails.user(), challengeInviteRequest);
        return ResponseEntity.ok().body("초대 완료 하였습니다");
    }

    @GetMapping("/invites/me")
    public ResponseEntity<List<ChallengeInviteResponse>> findInviteList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChallengeInviteResponse> myInviteList = challengeInviteService.getMyInviteList(userDetails.user());
        return ResponseEntity.ok().body(myInviteList);
    }

    @PatchMapping("/invites/{id}/accept")
    public ResponseEntity<?> acceptInvite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        challengeInviteService.acceptInvite(id, userDetails.user());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/invites/{id}/reject")
    public ResponseEntity<?> rejectInvite(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        challengeInviteService.rejectInvite(id, userDetails.user());
        return ResponseEntity.ok().build();
    }

}
