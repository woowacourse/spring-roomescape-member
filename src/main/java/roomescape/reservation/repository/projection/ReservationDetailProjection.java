package roomescape.reservation.repository.projection;

import roomescape.theme.dto.response.ThemeFindResponse;
import roomescape.reservationtime.dto.response.TimeInformation;

import java.time.LocalDate;

public record ReservationDetailProjection(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse themeFindResponse,
        TimeInformation timeInformation
) {
}
