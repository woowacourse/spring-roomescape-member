package roomescape.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;
import roomescape.member.model.Member;

public class Fixture {

    public static final ReservationTime RESERVATION_TIME_1 = new ReservationTime(1L, LocalTime.of(10, 0));
    public static final ReservationTime RESERVATION_TIME_2 = new ReservationTime(2L, LocalTime.of(12, 0));
    public static final Theme THEME_1 = new Theme(1L, "공포", "무서워",
            "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg");
    public static final Theme THEME_2 = new Theme(2L, "액션", "신나",
            "https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg");
    public static final Theme THEME_3 = new Theme(3L, "SF", "신기해",
            "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg");
    public static final Member MEMBER_1 = new Member(1L, "마크", "email1@woowa.com", "password");
    public static final Member MEMBER_2 = new Member(2L, "러너덕", "email2@woowa.com", "password");
    public static final Member MEMBER_3 = new Member(3L, "포비", "email3@woowa.com", "password");

    public static final Reservation RESERVATION_1 = new Reservation(1L, "마크", LocalDate.of(2124, 5, 1),
            RESERVATION_TIME_1, THEME_1);
    public static final Reservation RESERVATION_2 = new Reservation(2L, "러너덕", LocalDate.of(2124, 5, 2),
            RESERVATION_TIME_1, THEME_2);
    public static final Reservation RESERVATION_3 = new Reservation(3L, "포비", LocalDate.of(2124, 5, 3),
            RESERVATION_TIME_1, THEME_2);

}
