package dev.book.user.dto.response;

public record UserChallengeInfoResponse(long savings, int completedChallenges, int participatingChallenges, int finishedChallenge) {
}
