package dev.book.challenge.dto.request;

import java.time.LocalDate;

public record ChallengeCreateRequest(String title, String text, String release, Integer amount,
                                     Integer capacity,
                                     String category, String status, LocalDate startDate, LocalDate endDate) {

}
