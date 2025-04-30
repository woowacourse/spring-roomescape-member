package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public Reservation convertToReservation(final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }
}
