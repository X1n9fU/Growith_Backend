package dev.book.global.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class SseEmitterRepositoryImpl implements SseEmitterRepository{

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartsWithUserId(String id) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartsWithUserId(String id) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteEmitterById(String emitterId) {
        emitters.remove(emitterId);

    }

    @Override
    public void deleteAllEmitterStartsWithUserId(String id) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(id)){
                        emitters.remove(key);
                    }
                }
        );

    }

    @Override
    public void deleteAllEventCacheStartsWithUserId(String id) {
        eventCache.forEach(
                (key, value) -> {
                    if (key.startsWith(id)){
                        eventCache.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteEventCacheById(String id) {
        eventCache.remove(id);
    }
}
