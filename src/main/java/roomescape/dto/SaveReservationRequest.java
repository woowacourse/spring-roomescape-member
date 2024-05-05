package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record SaveReservationRequest(LocalDate date, String name, Long timeId, Long themeId) {
    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(
                name,
                date,
                reservationTime,
                theme
        );
    }
}
