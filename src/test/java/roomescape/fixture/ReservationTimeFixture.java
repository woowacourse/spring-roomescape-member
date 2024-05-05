package roomescape.fixture;

import static roomescape.fixture.DateTimeFixture.TIME_10_00;
import static roomescape.fixture.DateTimeFixture.TIME_11_00;
import static roomescape.fixture.DateTimeFixture.TIME_12_00;

import roomescape.domain.ReservationTime;

public class ReservationTimeFixture {

    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(1L, TIME_10_00);
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(2L, TIME_11_00);
    public static final ReservationTime RESERVATION_TIME_3 = new ReservationTime(3L, TIME_12_00);
}
