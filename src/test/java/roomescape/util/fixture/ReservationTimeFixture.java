package roomescape.util.fixture;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.time.domain.ReservationTime;

public class ReservationTimeFixture {

    private static final AtomicLong idSequence = new AtomicLong(1L);

    public static ReservationTime create(LocalTime time) {
        return new ReservationTime(idSequence.getAndIncrement(), time);
    }

    public static ReservationTime createWithId(Long id, LocalTime time) {
        return new ReservationTime(id, time);
    }
}
