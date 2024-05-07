package roomescape.fixture;

import java.time.LocalTime;
import roomescape.domain.time.ReservationTime;

public class ReservationTimeFixture {
    public static final ReservationTime DEFAULT_RESERVATION_TIME = reservationTime("13:00");

    public static ReservationTime reservationTime(String startAt) {
        return new ReservationTime(1L, LocalTime.parse(startAt));
    }
}
