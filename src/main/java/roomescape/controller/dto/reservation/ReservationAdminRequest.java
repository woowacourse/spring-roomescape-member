package roomescape.controller.dto.reservation;

import java.time.LocalDate;

public record ReservationAdminRequest(
        Long memberId,
        LocalDate date,
        Long themeId,
        Long timeId
) {
}
