package roomescape.time.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequest(
        @NotNull
        Long themeId,
        @NotNull
        LocalDate date
) {
}
