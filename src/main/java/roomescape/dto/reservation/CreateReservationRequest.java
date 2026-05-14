package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateReservationRequest(
        @NotBlank String name,
        @NotNull Long themeId,
        @NotNull LocalDate date,
        @NotNull Long timeId
) {
}
