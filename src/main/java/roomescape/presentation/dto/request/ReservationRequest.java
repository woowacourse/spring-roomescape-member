package roomescape.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank String name,
        @NotNull @FutureOrPresent LocalDate date,
        long timeId,
        long themeId
) {
}
