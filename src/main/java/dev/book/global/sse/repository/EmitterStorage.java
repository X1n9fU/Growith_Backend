package dev.book.global.sse.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmitterStorage {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public Map<String, SseEmitter> getEmitters() {
        return emitters;
    }

    public Map<String, Object> getEventCache() {
        return eventCache;
    }
}
