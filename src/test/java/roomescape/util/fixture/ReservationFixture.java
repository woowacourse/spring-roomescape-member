package roomescape.util.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

public class ReservationFixture {

    private static Long idSequence = 1L;

    public static Reservation createByDate(LocalDate date) {
        return new Reservation(idSequence++, "user", date, false,
                ReservationTimeFixture.create(LocalTime.now()),
                ThemeFixture.createDefault());
    }

    public static Reservation createCancelled() {
        return new Reservation(idSequence++, "user", LocalDate.now().plusDays(1), true,
                ReservationTimeFixture.create(LocalTime.now()),
                ThemeFixture.createDefault());
    }

    public static Reservation createByTime(ReservationTime reservationTime) {
        return new Reservation(idSequence++, "user", LocalDate.now().plusDays(1), false,
                reservationTime,
                ThemeFixture.createDefault());
    }
}
