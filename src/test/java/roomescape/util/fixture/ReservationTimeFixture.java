package roomescape.util.fixture;

import java.time.LocalTime;
import roomescape.time.domain.ReservationTime;

public class ReservationTimeFixture {

    private static Long idSequence = 1L;

    public static ReservationTime create(LocalTime time) {
        return new ReservationTime(idSequence++, time);
    }

    public static ReservationTime createWithId(Long id, LocalTime time) {
        return new ReservationTime(id, time);
    }
}
