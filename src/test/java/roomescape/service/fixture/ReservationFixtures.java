package roomescape.service.fixture;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationName;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class ReservationFixtures {

    public static Reservation createReservation(String name, String date, ReservationTime time, Theme theme) {
        return new Reservation(null, new ReservationName(name), ReservationDate.from(date), time, theme);
    }
}
