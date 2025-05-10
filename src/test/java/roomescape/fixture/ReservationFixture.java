package roomescape.fixture;

import java.time.LocalDate;
import roomescape.business.domain.member.MemberName;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationDateTime;

public class ReservationFixture {

    public static final Reservation RESERVATION = new Reservation(
            1L,
            new MemberName("사용자"),
            new ReservationDateTime(LocalDate.now().plusDays(1), TimeFixture.TIME),
            ThemeFixture.THEME
    );
}
