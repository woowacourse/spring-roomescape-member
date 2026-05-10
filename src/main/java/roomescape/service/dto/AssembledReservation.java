package roomescape.service.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record AssembledReservation(
        Reservation reservation,
        ReservationTime time,
        Theme theme
) {
}
