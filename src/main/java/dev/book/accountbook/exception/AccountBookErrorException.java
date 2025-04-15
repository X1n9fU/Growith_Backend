package dev.book.accountbook.exception;

import lombok.Getter;

@Getter
public class AccountBookErrorException extends CustomErrorException {
    private final AccountBookErrorCode errorCode;
    private final Object additionalDate;

    public AccountBookErrorException(AccountBookErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        this.errorCode = errorCode;
        this.additionalDate = null;
    }

    public AccountBookErrorException(AccountBookErrorCode errorCode, Object additionalDate) {
        super(errorCode.getMessage() + " " + additionalDate, errorCode);
        this.errorCode = errorCode;
        this.additionalDate = additionalDate;
    }
}
