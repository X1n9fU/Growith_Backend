package dev.book.challenge.dto.request;

import java.time.LocalDate;

public record ChallengeUpdateRequest(String title, String text, String release, Integer amount,
                                     Integer capacity,
                                     String category, LocalDate startDate, LocalDate endDate) {

}
