package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationSaveRequest(String name, LocalDate date, Long themeId, Long timeId) {

    public Reservation toReservation(Theme theme, ReservationTime reservationTime) {
        return new Reservation(new Name(name), date, theme, reservationTime);
    }
}
