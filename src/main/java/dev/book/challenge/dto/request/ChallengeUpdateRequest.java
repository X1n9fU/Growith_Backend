package dev.book.challenge.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ChallengeUpdateRequest(@NotNull String title, @NotNull String text, @NotNull String release,
                                     @Min(value = 1000, message = "1000원 이상 입력 가능합니다.") Integer amount,
                                     @Min(value = 1, message = "1명 부터 입력 가능합니다.") Integer capacity,
                                     @NotNull String category,
                                     @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

}
