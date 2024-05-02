package roomescape;

import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class InitialDataFixture {
    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(1L, LocalTime.parse("09:00"));
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(2L, LocalTime.parse("10:00"));
    public static final ReservationTime RESERVATION_TIME_3 = new ReservationTime(3L, LocalTime.parse("11:00"));

    public static final Theme THEME_1 = new Theme(1L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_2 = new Theme(2L, new Name("레벨3 탈출"), "우테코 레벨3를 탈출하는 내용입니다.", "아무 내용 없음");

    public static final Reservation RESERVATION_1 = new Reservation(1L, new Name("브라운"), LocalDate.parse("2024-04-25"), RESERVATION_TIME_1, THEME_1);
    public static final Reservation RESERVATION_2 = new Reservation(2L, new Name("솔라"), LocalDate.parse("2099-05-01"), RESERVATION_TIME_1, THEME_1);
}
