package roomescape.controller.dto.reservation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationAdminRequest(
        @NotNull Long memberId,
        @NotNull LocalDate date,
        @NotNull Long themeId,
        @NotNull Long timeId
) {
}
