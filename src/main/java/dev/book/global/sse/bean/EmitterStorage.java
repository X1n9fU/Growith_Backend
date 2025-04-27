package dev.book.global.sse.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class EmitterStorage {

    @Bean
    public Map<String, SseEmitter> emitters() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, Object> eventCache() {
        return new ConcurrentHashMap<>();
    }
}
