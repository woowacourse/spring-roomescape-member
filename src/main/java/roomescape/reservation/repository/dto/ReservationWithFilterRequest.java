package roomescape.reservation.repository.dto;

import java.time.LocalDate;

public record ReservationWithFilterRequest(
        Long memberId,
        Long themeId,
        LocalDate from,
        LocalDate to
) {
}
