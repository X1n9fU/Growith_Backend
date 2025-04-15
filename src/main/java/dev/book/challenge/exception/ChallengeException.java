package dev.book.challenge.exception;

import lombok.Getter;

@Getter
public class ChallengeException extends RuntimeException {

    private final ErrorCode errorCode;

    public ChallengeException(ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }
}
