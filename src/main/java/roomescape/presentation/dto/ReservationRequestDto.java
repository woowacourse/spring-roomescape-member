package roomescape.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequestDto(
        @NotNull LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
}
