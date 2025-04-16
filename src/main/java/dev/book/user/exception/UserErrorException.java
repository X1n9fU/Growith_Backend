package dev.book.user.exception;

import lombok.Getter;

@Getter
public class UserErrorException extends RuntimeException{
    private final UserErrorCode userErrorCode;
    private final Object additionalDate;

    public UserErrorException(UserErrorCode errorCode){
        super(errorCode.getMessage());
        this.userErrorCode=errorCode;
        this.additionalDate=null;
    }
    public UserErrorException(UserErrorCode userErrorCode, Object additionalDate){
        super(userErrorCode.getMessage());
        this.userErrorCode = userErrorCode;
        this.additionalDate = additionalDate;
    }
}
