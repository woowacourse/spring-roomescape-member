package roomescape.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
        @NotNull LocalDate date,
        @NotNull long timeId,
        @NotNull long themeId,
        @NotNull long memberId
) {
}
