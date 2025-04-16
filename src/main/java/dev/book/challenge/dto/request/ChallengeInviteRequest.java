package dev.book.challenge.dto.request;

import jakarta.validation.constraints.Email;

public record ChallengeInviteRequest(@Email(message = "올바른 이메일 형식이 아닙니다.") String email) {
}
