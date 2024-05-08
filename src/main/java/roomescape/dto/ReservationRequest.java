package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.LoginUser;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationRequest(LocalDate date, long timeId, long themeId) {
    public Reservation toReservation(LoginUser user, ReservationTime reservationTime, Theme theme) {
        return new Reservation(user.getName(), this.date, reservationTime, theme);
    }
}
