package roomescape.domain.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public final class ReservationTimeFixture {

    private ReservationTimeFixture() {
    }

    public static ReservationTime createDefaultReservationTime() {
        return ReservationTime.restore(1L, LocalTime.of(10, 0), true);
    }
}
