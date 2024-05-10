package roomescape.fixture;

import static roomescape.fixture.MemberBuilder.DEFAULT_MEMBER;
import static roomescape.fixture.ReservationTimeBuilder.DEFAULT_TIME;
import static roomescape.fixture.ThemeBuilder.DEFAULT_THEME;

import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationBuilder {
    public static final Reservation DEFAULT_RESERVATION_WITHOUT_ID = withOutId(DEFAULT_MEMBER, LocalDate.now(),
            DEFAULT_TIME, DEFAULT_THEME);

    public static Reservation withOutId(Member reservationMember, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(reservationMember, date, time, theme);
    }

    public static Reservation withId(long id, Member reservationMember, LocalDate date, ReservationTime time,
                                     Theme theme) {
        return new Reservation(reservationMember, date, time, theme);
    }
}
