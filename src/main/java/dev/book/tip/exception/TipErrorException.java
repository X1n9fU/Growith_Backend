package dev.book.tip.exception;

import dev.book.global.exception.CustomErrorException;

public class TipErrorException extends CustomErrorException {

    private final TipErrorCode tipErrorCode;

    public TipErrorException(TipErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        this.tipErrorCode = errorCode;
    }
}
