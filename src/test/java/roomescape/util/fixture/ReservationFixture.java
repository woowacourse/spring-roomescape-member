package roomescape.util.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

public class ReservationFixture {

    private static Long idSequence = 1L;

    public static Reservation createByTime(ReservationTime reservationTime) {
        return new Reservation(idSequence++, "user", LocalDate.now().plusDays(1), false,
                reservationTime,
                ThemeFixture.createDefault());
    }
}
