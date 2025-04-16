package dev.book.friend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record EncryptUserInfo(String email, Long id, LocalDateTime localDateTime){

    @JsonCreator
    public EncryptUserInfo(
            @JsonProperty("email") String email,
            @JsonProperty("id") Long id,
            @JsonProperty("localDateTime") LocalDateTime localDateTime
    ){
        this.email = email;
        this.id = id;
        this.localDateTime = localDateTime;
    }
}