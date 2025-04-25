package dev.book.global.sse.dto;

public record SseResponse (String lastEventId, String title, String content, String name){
}
