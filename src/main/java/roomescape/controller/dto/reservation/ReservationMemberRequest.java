package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationMemberRequest(
        @NotNull LocalDate date,
        long timeId,
        long themeId
) {
}
