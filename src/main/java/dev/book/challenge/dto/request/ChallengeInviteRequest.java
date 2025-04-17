package dev.book.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record ChallengeInviteRequest(@Schema(defaultValue = "test@gmail.com")@Email(message = "올바른 이메일 형식이 아닙니다.") String email) {
}
