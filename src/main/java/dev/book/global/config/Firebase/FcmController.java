package dev.book.global.config.Firebase;

import dev.book.global.config.Firebase.service.FCMService;
import dev.book.global.config.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {
    private final FCMService fcmService;

    @PostMapping("/token")
    public void saveToken(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String token) {
        fcmService.saveToken(userDetails.user(), token);
    }
}
