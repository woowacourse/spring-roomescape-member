package roomescape;

import roomescape.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class InitialDataFixture {
    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(1L, LocalTime.parse("09:00"));
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(2L, LocalTime.parse("10:00"));
    public static final ReservationTime RESERVATION_TIME_3 = new ReservationTime(3L, LocalTime.parse("11:00"));

    public static final Theme THEME_1 = new Theme(1L, new Name("레벨1 탈출"), "우테코 레벨1를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_2 = new Theme(2L, new Name("레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_3 = new Theme(3L, new Name("레벨3 탈출"), "우테코 레벨3를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_4 = new Theme(4L, new Name("레벨4 탈출"), "우테코 레벨4를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_5 = new Theme(5L, new Name("레벨5 탈출"), "우테코 레벨5를 탈출하는 내용입니다.", "아무 내용 없음");
    public static final Theme THEME_6 = new Theme(6L, new Name("레벨6 탈출"), "우테코 레벨6를 탈출하는 내용입니다.", "아무 내용 없음");

    public static final Member MEMBER_1 = new Member(1L, new Name("브라운"), new Email("brown@test.com"), Role.USER, "pass");
    public static final Member MEMBER_2 = new Member(2L, new Name("솔라"), new Email("sola@test.com"), Role.ADMIN, "pass");

    public static final Reservation RESERVATION_1 = new Reservation(1L, MEMBER_1, LocalDate.parse("2024-04-25"), RESERVATION_TIME_1, THEME_1);
    public static final Reservation RESERVATION_2 = new Reservation(2L, MEMBER_2, LocalDate.parse("2099-05-01"), RESERVATION_TIME_1, THEME_1);
}
