package dev.book.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "유저 회원가입 RequestDto")
public record UserSignUpRequest(

        @Schema(description = "유저 닉네임", defaultValue = "growith")
        @NotNull
        String nickname,

        @Schema(description = "유저 초기 카테고리", defaultValue = "[\n" +
                "         \"food\", \"cafe_snack\", \"convenience_store\", \"shopping\", \"hobby\", \"living\", \"beauty\", \"health\"\n" +
                "   ]")
        List<String> categories
) {
}
