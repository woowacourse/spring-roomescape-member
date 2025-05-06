package roomescape.reservation.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class TestFixture {

    private static final String DATE_FORMAT = "%d-%02d-%02d";
    private static final LocalTime TIME = LocalTime.of(10, 0);

    public static Theme makeTheme() {
        return Theme.of(1L, "추리", "셜록 추리 게임 with Danny", "image.png");
    }

    public static String makeTomorrowMessage() {
        LocalDate today = LocalDate.now().plusDays(1);
        return String.format(DATE_FORMAT, today.getYear(), today.getMonthValue(), today.getDayOfMonth());
    }

    public static Reservation makeReservation(final Long reservationId, final long reservationTimeId) {
        ReservationTime reservationTime = makeReservationTime(reservationTimeId);
        return Reservation.of(reservationId, "밍트", LocalDate.now().plusDays(1), reservationTime, makeTheme());
    }

    public static ReservationTime makeReservationTime(final long reservationTimeId) {
        return ReservationTime.of(reservationTimeId, TIME);
    }
}
