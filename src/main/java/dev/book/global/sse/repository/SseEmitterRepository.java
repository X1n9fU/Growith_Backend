package dev.book.global.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseEmitterRepository{
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitterStartsWithUserId(String id);

    Map<String, Object> findAllEventCacheStartsWithUserId(String id);

    void deleteEmitterById(String id);

    void deleteAllEmitterStartsWithUserId(String id);

    void deleteAllEventCacheStartsWithUserId(String id);

    void deleteEventCacheById(String id);

}
