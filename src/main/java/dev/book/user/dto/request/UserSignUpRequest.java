package dev.book.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserSignUpRequest(

        @Email @NotNull
        String email,

        @NotNull
        String nickname,

        @NotNull
        String category //todo 카테고리로 변경
) {
}
