package dev.book.global.sse.service;

import dev.book.achievement.dto.AchievementResponseDto;
import dev.book.global.sse.dto.response.SseAchievementResponse;
import dev.book.global.sse.repository.SseEmitterRepository;
import dev.book.global.sse.type.SseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SseServiceTest {

    @Mock
    SseEmitterRepository sseEmitterRepository;

    @InjectMocks
    SseService sseService;

    @Test
    @DisplayName("구독 후 lastEventId 이후에 발생한 이벤트들도 전송한다.")
    void subscribeAndSendEvent() throws IOException {
        //given
        Long userId = 1L;
        String lastEventId = "1_1";

        SseEmitter sseEmitter = mock(SseEmitter.class);
        Map<String, Object> eventCache = Map.of(
                "1_2", new SseAchievementResponse("1_2", "업적1", "내용", SseType.ACHIEVEMENT.name())
        );

        given(sseEmitterRepository.save(any(), any())).willReturn(sseEmitter);
        given(sseEmitterRepository.findAllEventCacheStartsWithUserId(String.valueOf(userId))).willReturn(eventCache);

        //when
        SseEmitter result = sseService.subscribe(userId, lastEventId);

        //then
        assertThat(result).isNotNull();
        //eventCache의 event가 전송되었다면 발생했을 내역 체크
        verify(sseEmitter, times(2)).send(any(SseEmitter.SseEventBuilder.class)); //전송 로직 추가
        verify(sseEmitterRepository).save(anyString(), any(SseEmitter.class));
        verify(sseEmitterRepository).findAllEventCacheStartsWithUserId(String.valueOf(userId));
        verify(sseEmitterRepository).deleteAllEventCacheStartsWithUserId(String.valueOf(userId));
    }

    @Test
    @DisplayName("emitter가 없을 경우 eventCache에만 저장한다.")
    void sendWithNoEmitter() {
        //given
        Long userId = 1L;
        AchievementResponseDto achievementResponseDto = new AchievementResponseDto("업적1", "내용");

        given(sseEmitterRepository.findAllEmitterStartsWithUserId(String.valueOf(userId))).willReturn(Map.of());

        //when
        sseService.sendAchievementToUser(userId, achievementResponseDto);

        //then
        verify(sseEmitterRepository).saveEventCache(anyString(), any(SseAchievementResponse.class));
    }

    @Test
    @DisplayName("emitter가 있을 경우 바로 event를 전송한다.")
    void sendWithEmitter() throws IOException {
        //given
        Long userId = 1L;
        AchievementResponseDto achievementResponseDto = new AchievementResponseDto("업적1", "내용");

        SseEmitter sseEmitter = mock(SseEmitter.class);
        Map<String, SseEmitter> sseEmitterMap = Map.of("1_1", sseEmitter);
        given(sseEmitterRepository.findAllEmitterStartsWithUserId(String.valueOf(userId))).willReturn(sseEmitterMap);

        //when
        sseService.sendAchievementToUser(userId, achievementResponseDto);

        //then
        verify(sseEmitter).send(any(SseEmitter.SseEventBuilder.class)); //전송 로직 추가
        verify(sseEmitterRepository).saveEventCache(anyString(), any(SseAchievementResponse.class));
    }

}