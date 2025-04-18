package dev.book.user.dto.request;

import dev.book.accountbook.type.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "유저 회원가입 RequestDto")
public record UserSignUpRequest(

        @Schema(description = "유저 닉네임", defaultValue = "growith")
        @NotNull
        String nickname,

        @Schema(description = "유저 초기 카테고리", defaultValue = "[\n" +
                "         \"식비\", \"카페 / 간식\", \"편의점 / 마트 / 잡화\", \"쇼핑\", \"취미 / 여가\", \"생활\", \"미용\", \"의료 / 건강 / 피트니스\"\n" +
                "   ]")
        List<Category> categories
) {
}
