package dev.book.challenge.dto.request;

public record ChallengeCreateRequest(String title, String text, String release, Integer amount,
                                     Integer capacity,
                                     String category, String status) {

}
