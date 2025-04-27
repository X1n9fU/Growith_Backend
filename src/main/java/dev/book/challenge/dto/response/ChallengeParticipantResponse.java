package dev.book.challenge.dto.response;

public record ChallengeParticipantResponse(Long id, String name, long totalSpend, long amount, int endDay,
                                           boolean isSuccess, boolean isWriteTip) {
}
