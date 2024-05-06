package roomescape.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public class ReservationFixture {
    public static Reservation getNextDayReservation(ReservationTime time, Theme theme) {
        return new Reservation(1L, LocalDate.now().plusDays(1), time, theme);
    }
}
