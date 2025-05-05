package roomescape.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull @FutureOrPresent LocalDate date,
        long timeId,
        long themeId,
        long userId
) {
}
