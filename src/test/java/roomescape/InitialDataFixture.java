package roomescape;

import roomescape.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class InitialDataFixture {
    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(null, LocalTime.parse("09:33"));
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(null, LocalTime.parse("10:33"));
    public static final ReservationTime RESERVATION_TIME_3 = new ReservationTime(null, LocalTime.parse("11:33"));

    public static final Theme THEME_1 = new Theme(null, new Name("test 레벨1 탈출"), "우테코 레벨1를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final Theme THEME_2 = new Theme(null, new Name("test 레벨2 탈출"), "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final Theme THEME_3 = new Theme(null, new Name("test 레벨3 탈출"), "우테코 레벨3를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final Theme THEME_4 = new Theme(null, new Name("test 레벨4 탈출"), "우테코 레벨4를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final Theme THEME_5 = new Theme(null, new Name("test 레벨5 탈출"), "우테코 레벨5를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final Theme THEME_6 = new Theme(null, new Name("test 레벨6 탈출"), "우테코 레벨6를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

    public static final Member USER_1 = new Member(null, new Name("브라운"), new Email("brown@test.com"), Role.USER, "pass");
    public static final Member ADMIN_1 = new Member(null, new Name("솔라"), new Email("sola@test.com"), Role.ADMIN, "pass");
}
