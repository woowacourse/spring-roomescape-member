package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class InitialDataFixture {

    public static final int INITIAL_THEME_COUNT = 3;
    public static final int INITIAL_RESERVATION_TIME_COUNT = 3;
    public static final int INITIAL_RESERVATION_COUNT = 2;
    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(1L, LocalTime.parse("09:00"));
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(2L, LocalTime.parse("10:00"));
    public static final ReservationTime NOT_RESERVATION_TIME = new ReservationTime(3L, LocalTime.parse("11:00"));
    public static final LocalDate NO_RESERVATION_DATE = LocalDate.parse("2020-01-01");

    public static final Theme THEME_1 = new Theme(
            1L,
            new Name("레벨1 탈출"),
            "우테코 레벨1를 탈출하는 내용입니다.",
            "아무 내용 없음"
    );
    public static final Theme THEME_2 = new Theme(
            2L,
            new Name("레벨2 탈출"),
            "우테코 레벨2를 탈출하는 내용입니다.",
            "아무 내용 없음"
    );
    public static final Theme NOT_RESERVATION_THEME = new Theme(
            3L,
            new Name("레벨3 탈출"),
            "우테코 레벨3를 탈출하는 내용입니다.",
            "아무 내용 없음"
    );

    public static final Reservation RESERVATION_1 = new Reservation(
            1L,
            new Name("브라운"),
            LocalDate.parse("2024-04-25"),
            RESERVATION_TIME_1,
            THEME_1
    );
    public static final Reservation RESERVATION_2 = new Reservation(
            2L,
            new Name("솔라"),
            LocalDate.parse("2099-05-01"),
            RESERVATION_TIME_1,
            THEME_1
    );
}
