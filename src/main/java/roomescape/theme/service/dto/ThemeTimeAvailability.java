package roomescape.theme.service.dto;

import roomescape.reservation.domain.ReservationTime;

public record ThemeTimeAvailability(
        ReservationTime reservationTime,
        boolean isAvailable
) {
}
