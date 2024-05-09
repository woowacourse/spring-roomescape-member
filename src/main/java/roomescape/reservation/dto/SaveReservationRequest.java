package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public record SaveReservationRequest(LocalDate date, String name, Long timeId, Long themeId) {
    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.createInstanceWithoutId(
                name,
                date,
                reservationTime,
                theme
        );
    }
}
