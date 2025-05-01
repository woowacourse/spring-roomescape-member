package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull long timeId,
        @NotNull long themeId
) {
}
