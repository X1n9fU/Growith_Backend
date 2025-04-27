package dev.book.global.sse.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SseEmitterRepositoryImpl implements SseEmitterRepository{

    private final EmitterStorage emitterStorage;

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitterStorage.emitters().put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        emitterStorage.eventCache().put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartsWithUserId(String id) {
        return emitterStorage.emitters().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartsWithUserId(String id) {
        return emitterStorage.eventCache().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, SseEmitter> findAllEmitters() {
        return emitterStorage.emitters();
    }

    @Override
    public void deleteEmitterById(String emitterId) {
        emitterStorage.emitters().remove(emitterId);

    }

    @Override
    public void deleteAllEmitterStartsWithUserId(String id) {
        emitterStorage.emitters().keySet().removeIf(key -> key.startsWith(id));


    }

    @Override
    public void deleteAllEventCacheStartsWithUserId(String id) {
        emitterStorage.eventCache().keySet().removeIf(key -> key.startsWith(id));

    }

    @Override
    public void deleteEventCacheById(String id) {
        emitterStorage.eventCache().remove(id);
    }
}
