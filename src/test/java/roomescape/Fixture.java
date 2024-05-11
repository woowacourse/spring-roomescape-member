package roomescape;

import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberEmail;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.MemberPassword;
import roomescape.domain.vo.MemberRole;
import roomescape.domain.vo.ReservationDate;
import roomescape.domain.vo.ReservationTime;

public class Fixture {

    public static final Theme VALID_THEME = new Theme("방탈출", "방탈출하는 게임",
        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static final ReservationDate VALID_RESERVATION_DATE = new ReservationDate("2040-01-01");
    public static final ReservationTime VALID_RESERVATION_TIME = new ReservationTime("05:30");
    public static final MemberName VALID_USER_NAME = new MemberName("wiib");
    public static final MemberEmail VALID_USER_EMAIL = new MemberEmail("repday0609@gmail.com");
    public static final MemberPassword VALID_USER_PASSWORD = new MemberPassword("1!2@3#");
    public static final MemberRole VALID_USER_ROLE = new MemberRole("USER");
    public static final MemberName VALID_ADMIN_NAME = new MemberName("stitch");
    public static final MemberEmail VALID_ADMIN_EMAIL = new MemberEmail("stitch@gmail.com");
    public static final MemberPassword VALID_ADMIN_PASSWORD = new MemberPassword("admin123");
    public static final MemberRole VALID_ADMIN_ROLE = new MemberRole("ADMIN");
    public static final Member VALID_MEMBER = new Member(VALID_USER_NAME, VALID_USER_EMAIL,
        VALID_USER_PASSWORD,
        VALID_USER_ROLE);
    public static final Reservation VALID_RESERVATION = new Reservation(VALID_MEMBER, VALID_RESERVATION_DATE,
        VALID_RESERVATION_TIME, VALID_THEME);

    public static final String COOKIE_NAME = "token";

}
