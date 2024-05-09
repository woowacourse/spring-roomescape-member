package roomescape.reservation.dto;

import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;

public record SaveReservationRequest(LocalDate date, String name, Long timeId, Long themeId) {

    public SaveReservationRequest setClientName(final String clientName) {
        return new SaveReservationRequest(date, clientName, timeId, themeId);
    }

    public Reservation toReservation(final ReservationTime reservationTime, final Theme theme) {
        return Reservation.of(
                name,
                date,
                reservationTime,
                theme
        );
    }
}
