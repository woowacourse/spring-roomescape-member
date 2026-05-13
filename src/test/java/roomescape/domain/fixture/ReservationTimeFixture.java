package roomescape.domain.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public final class ReservationTimeFixture {

    private ReservationTimeFixture() {
    }

    public static ReservationTime createDefaultReservationTime() {
        return new ReservationTime(1L, LocalTime.of(10, 0));
    }

    public static ReservationTime createReservationTime(LocalTime startAt) {
        return new ReservationTime(startAt);
    }

    public static ReservationTime createReservationTimeWithId(Long id, LocalTime startAt) {
        return new ReservationTime(id, startAt);
    }
}
