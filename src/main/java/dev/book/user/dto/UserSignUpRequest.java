package dev.book.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserSignUpRequest(

        @Email
        String email,

        @NotNull
        String nickname,

        @NotNull
        String category //todo 카테고리로 변경
) {
}
