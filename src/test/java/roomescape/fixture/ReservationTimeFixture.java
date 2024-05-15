package roomescape.fixture;

import roomescape.reservation.domain.ReservationTime;
import java.time.LocalTime;

public class ReservationTimeFixture {
    public static final ReservationTime reservationTimeFixture = new ReservationTime(1L, LocalTime.of(10,30));
}
