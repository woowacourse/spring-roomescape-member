package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationRequestDto(
    @NotNull LocalDate date,
    @Size(min = 1, max = 10) String name,
    @NotNull Long timeId) {
}
