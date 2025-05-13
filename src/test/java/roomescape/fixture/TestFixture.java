package roomescape.fixture;

import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestFixture {

    public static final ReservationTime DEFAULT_TIME = new ReservationTime(LocalTime.of(13, 5));
    public static final Theme DEFAULT_THEME = new Theme("폭싹 속았수다", "감동적인 내용", "abc");
    public static final Reservation DEFAULT_RESERVATION = new Reservation(1L, LocalDate.now().plusDays(1), DEFAULT_TIME, DEFAULT_THEME);

    public static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    public static final LocalDate YESTERDAY = LocalDate.now().plusDays(-1);
    public static final LocalTime ONE_PM = LocalTime.of(13, 0);
}
