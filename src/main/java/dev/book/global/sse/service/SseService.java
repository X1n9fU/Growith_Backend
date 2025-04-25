package dev.book.global.sse.service;

import dev.book.achievement.dto.AchievementResponseDto;
import dev.book.global.sse.dto.SseResponse;
import dev.book.global.sse.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseEmitterRepository sseEmitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    /**
     * SSE 구독 경로, 구독한 emitter(유저)를 저장하고 Last Event Id보다 이후에 발생한 event들을 전송합니다.
     * @param userId
     * @param lastEventId
     * @return
     */
    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = sseEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> sseEmitterRepository.deleteEmitterById(emitterId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteEmitterById(emitterId));
        emitter.onError((e) -> sseEmitterRepository.deleteEmitterById(emitterId));

        sendToClient(emitter, emitterId, "EventStream created. [userId= "+ userId + "]", null);
        if (!lastEventId.isEmpty()){
            Map<String, Object> events = sseEmitterRepository.findAllEventCacheStartsWithUserId(String.valueOf(userId));
            events.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey())<0)
                .forEach(entry -> {
                    SseResponse event = (SseResponse) entry.getValue();
                    sendToClient(emitter, entry.getKey(), event, event.name());
                });
            sseEmitterRepository.deleteAllEventCacheStartsWithUserId(String.valueOf(userId));
        }

        return emitter;
    }

    /**
     * emitter(userId) 에게 event(AchievementResponseDto) 를 전송합니다.
     * @param userId
     * @param achievement
     */
    public void send(Long userId, AchievementResponseDto achievement, String name) {
        Map<String, SseEmitter> sseEmitters = sseEmitterRepository.findAllEmitterStartsWithUserId(String.valueOf(userId));
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseResponse sseResponse = new SseResponse(emitterId, achievement.title(), achievement.content(), name);
        if (sseEmitters.isEmpty()){ //emitter가 존재하지 않을 경우, 백그라운드에서 SSE 이벤트가 발생하였을 경우
            sseEmitterRepository.saveEventCache(emitterId, sseResponse); //event 캐시에만 저장해놓고 subscribe 되었을 때 반환하도록 한다.
        }
        else {
            sseEmitters.forEach((key, emitter) -> {
                sseEmitterRepository.saveEventCache(emitterId, sseResponse);
                sendToClient(emitter, emitterId, sseResponse, sseResponse.name());
            });
        }
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object value, String name) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name(name)
                    .data(value));
            sseEmitterRepository.deleteEventCacheById(emitterId); //전송한 event는 삭제
        } catch (IOException | IllegalStateException e){
            sseEmitterRepository.deleteEmitterById(emitterId);
        }
    }

}
