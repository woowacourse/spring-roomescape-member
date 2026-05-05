package roomescape.domain.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public class ReservationTimeFixture {

    public static ReservationTime createDefaultReservationTime() {
        return new ReservationTime(1L, LocalTime.of(10, 0));
    }
}
