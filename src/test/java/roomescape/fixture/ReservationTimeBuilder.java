package roomescape.fixture;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public class ReservationTimeBuilder {
    public static final ReservationTime DEFAULT_TIME = new ReservationTime(1L, LocalTime.of(11, 56));
}
