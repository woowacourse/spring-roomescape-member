package roomescape.fixture;

import static roomescape.fixture.DateTimeFixture.TOMORROW;
import static roomescape.fixture.DateTimeFixture.YESTERDAY;
import static roomescape.fixture.ReservationTimeFixture.RESERVATION_TIME_10_00_ID_1;
import static roomescape.fixture.ReservationTimeFixture.RESERVATION_TIME_11_00_ID_2;
import static roomescape.fixture.ThemeFixture.THEME_1;
import static roomescape.fixture.ThemeFixture.THEME_2;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationAddRequest;

public class ReservationFixture {

    public static final ReservationAddRequest RESERVATION_REQUEST_1 = new ReservationAddRequest(
            "썬", TOMORROW,
            1L,
            1L);

    public static final ReservationAddRequest PAST_DATE_RESERVATION_REQUEST = new ReservationAddRequest(
            "썬",
            YESTERDAY,
            1L,
            1L);

    public static final Reservation SAVED_RESERVATION_1 = new Reservation(
            1L,
            "썬",
            TOMORROW,
            RESERVATION_TIME_10_00_ID_1,
            THEME_1);

    public static final Reservation SAVED_RESERVATION_2 = new Reservation(
            2L,
            "리비",
            TOMORROW,
            RESERVATION_TIME_10_00_ID_1,
            THEME_2);

    public static final Reservation SAVED_RESERVATION_3 = new Reservation(
            3L,
            "도도",
            TOMORROW,
            RESERVATION_TIME_11_00_ID_2,
            THEME_1);
}
