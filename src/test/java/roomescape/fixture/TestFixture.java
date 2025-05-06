package roomescape.fixture;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestFixture {

    public static final ReservationTime DEFAULT_TIME = new ReservationTime(LocalTime.of(13, 5));
    public static final Theme DEFAULT_THEME = new Theme("폭싹 속았수다", "감동적인 내용", "abc");
    public static final Reservation DEFAULT_RESERVATION = new Reservation("메이", LocalDate.now().plusDays(1), DEFAULT_TIME, DEFAULT_THEME);

    public static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    public static final LocalTime NOON = LocalTime.NOON;
}
