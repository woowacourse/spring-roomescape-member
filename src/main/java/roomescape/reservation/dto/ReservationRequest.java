package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public record ReservationRequest(String name, LocalDate date, Long themeId, Long timeId) {

    public Reservation toReservation(Theme theme, ReservationTime reservationTime) {
        return new Reservation(new Name(name), date, theme, reservationTime);
    }
}
