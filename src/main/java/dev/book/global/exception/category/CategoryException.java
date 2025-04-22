package dev.book.global.exception.category;

import dev.book.global.exception.CustomErrorException;
import lombok.Getter;

@Getter
public class CategoryException extends CustomErrorException {

    private final CategoryErrorCode categoryErrorCode;
    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
        this.categoryErrorCode = errorCode;
    }
}
