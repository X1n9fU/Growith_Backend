package dev.book.global.sse.dto.response;

public record SseTipResponse (String lastEventId, String writer, String content, String name){
}
