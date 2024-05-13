package roomescape.fixture;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class ReservationFixture {
    public static final Reservation DEFAULT_RESERVATION = reservation("2023-04-23",
            ReservationTimeFixture.DEFAULT_RESERVATION_TIME, ThemeFixture.DEFAULT_THEME);

    public static Reservation reservation(String date, ReservationTime time, Theme theme) {
        return new Reservation(1L, LocalDate.parse(date), time, theme, MemberFixture.DEFAULT_MEMBER);
    }
}
