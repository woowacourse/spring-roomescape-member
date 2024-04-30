package roomescape;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class TestFixture {
    public static final String USER_MIA = "미아";
    public static final LocalDate MIA_RESERVATION_DATE = LocalDate.of(2030, Month.APRIL, 18);
    public static final LocalTime MIA_RESERVATION_TIME = LocalTime.of(15, 0);

    public static final String USER_TOMMY = "토미";
    public static final LocalDate TOMMY_RESERVATION_DATE = LocalDate.of(2030, Month.MAY, 19);
    public static final LocalTime TOMMY_RESERVATION_TIME = LocalTime.of(15, 0);

    public static Reservation MIA_RESERVATION() {
        return MIA_RESERVATION(new ReservationTime(MIA_RESERVATION_TIME));
    }

    public static Reservation MIA_RESERVATION(ReservationTime time) {
        return new Reservation(USER_MIA, MIA_RESERVATION_DATE, time);
    }

    public static Reservation TOMMY_RESERVATION() {
        return new Reservation(USER_TOMMY, TOMMY_RESERVATION_DATE, new ReservationTime(TOMMY_RESERVATION_TIME));
    }
}
