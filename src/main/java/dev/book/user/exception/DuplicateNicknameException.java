package dev.book.user.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(String msg) {
        super(msg);
    }
}
