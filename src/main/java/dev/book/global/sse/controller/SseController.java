package dev.book.global.sse.controller;

import dev.book.achievement.dto.AchievementResponseDto;
import dev.book.achievement.entity.Achievement;
import dev.book.achievement.repository.AchievementRepository;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.sse.service.SseService;
import dev.book.global.sse.type.SseType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class SseController {

    private final SseService sseService;
    private final AchievementRepository achievementRepository;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(sseService.subscribe(userDetails.user().getId(), lastEventId));
    }

    @GetMapping("/send")
    public ResponseEntity<?> send(@AuthenticationPrincipal CustomUserDetails userDetails){
        Optional<Achievement> achievement = achievementRepository.findById(1L);
        AchievementResponseDto achievementResponseDto = AchievementResponseDto.from(achievement.get());
        sseService.send(userDetails.user().getId(), achievementResponseDto, SseType.ACHIEVEMENT.name());
        return null;
    }
}
