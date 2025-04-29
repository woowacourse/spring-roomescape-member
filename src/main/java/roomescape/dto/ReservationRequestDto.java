package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReservationRequestDto(
    @Size(min = 1, max = 10) String name,
    @NotNull LocalDate date,
    @NotNull Long timeId,
    @NotNull Long themeId
) {
}
