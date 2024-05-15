package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.dto.response.ThemeResponse;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

public class TestFixtures {
    public static final ReservationTime TIME_1 = new ReservationTime(1L, LocalTime.of(10, 0));
    public static final ReservationTime TIME_2 = new ReservationTime(2L, LocalTime.of(11, 0));
    public static final ReservationTime TIME_3 = new ReservationTime(3L, LocalTime.of(12, 0));
    public static final Theme THEME_1 = new Theme(
            1L, "name1", "description1", "thumbnail1"
    );
    public static final Theme THEME_2 = new Theme(
            2L, "name2", "description2", "thumbnail2"
    );
    public static final Theme THEME_3 = new Theme(
            3L, "name3", "description3", "thumbnail3"
    );
    public static final ThemeResponse THEME_RESPONSE_1 = new ThemeResponse(
            1L, "name1", "description1", "thumbnail1"
    );
    public static final ThemeResponse THEME_RESPONSE_2 = new ThemeResponse(
            2L, "name2", "description2", "thumbnail2"
    );
    public static final ThemeResponse THEME_RESPONSE_3 = new ThemeResponse(
            3L, "name3", "description3", "thumbnail3"
    );
    public static final ThemeResponse THEME_RESPONSE_4 = new ThemeResponse(
            4L, "name4", "description4", "thumbnail4"
    );
    public static final ThemeResponse THEME_RESPONSE_5 = new ThemeResponse(
            5L, "name5", "description5", "thumbnail5"
    );
    public static final ThemeResponse THEME_RESPONSE_6 = new ThemeResponse(
            6L, "name6", "description6", "thumbnail6"
    );
    public static final ThemeResponse THEME_RESPONSE_7 = new ThemeResponse(
            7L, "name7", "description7", "thumbnail7"
    );
    public static final ThemeResponse THEME_RESPONSE_8 = new ThemeResponse(
            8L, "name8", "description8", "thumbnail8"
    );
    public static final ThemeResponse THEME_RESPONSE_9 = new ThemeResponse(
            9L, "name9", "description9", "thumbnail9"
    );
    public static final ThemeResponse THEME_RESPONSE_10 = new ThemeResponse(
            10L, "name10", "description10", "thumbnail10"
    );
    public static final ThemeResponse THEME_RESPONSE_11 = new ThemeResponse(
            11L, "name11", "description11", "thumbnail11"
    );
    public static final List<ThemeResponse> THEME_RESPONSES_1 = List.of(
            THEME_RESPONSE_1, THEME_RESPONSE_2, THEME_RESPONSE_3, THEME_RESPONSE_4, THEME_RESPONSE_5, THEME_RESPONSE_6,
            THEME_RESPONSE_7, THEME_RESPONSE_8, THEME_RESPONSE_9
    );
    public static final List<ThemeResponse> THEME_RESPONSES_2 = List.of(
            THEME_RESPONSE_1, THEME_RESPONSE_2, THEME_RESPONSE_3, THEME_RESPONSE_4, THEME_RESPONSE_5, THEME_RESPONSE_6,
            THEME_RESPONSE_7, THEME_RESPONSE_8, THEME_RESPONSE_9, THEME_RESPONSE_10
    );
    public static final List<ThemeResponse> THEME_RESPONSES_3 = List.of(
            THEME_RESPONSE_1, THEME_RESPONSE_2, THEME_RESPONSE_3, THEME_RESPONSE_11, THEME_RESPONSE_4, THEME_RESPONSE_5,
            THEME_RESPONSE_6, THEME_RESPONSE_7, THEME_RESPONSE_8, THEME_RESPONSE_9
    );
    public static final Member MEMBER_1 = new Member(1L, "admin", "admin", "admin", Role.ADMIN);
    public static final Member MEMBER_2 = new Member(2L, "name1", "email1", "qq1", Role.USER);
    public static final Member MEMBER_3 = new Member(3L, "name2", "email2", "qq2", Role.USER);
    public static final Reservation RESERVATION_1 = new Reservation(
            1L, LocalDate.now().plusDays(5), MEMBER_1, TIME_1, THEME_1
    );
    public static final Reservation RESERVATION_2 = new Reservation(
            2L, LocalDate.now().plusDays(6), MEMBER_1, TIME_2, THEME_2
    );
    public static final Reservation RESERVATION_3 = new Reservation(
            3L, LocalDate.now().plusDays(7), MEMBER_2, TIME_2, THEME_3
    );
    public static final Reservation RESERVATION_4 = new Reservation(
            4L, LocalDate.now().plusDays(8), MEMBER_2, TIME_3, THEME_1
    );
    public static final Reservation RESERVATION_5 = new Reservation(
            5L, LocalDate.now().plusDays(9), MEMBER_2, TIME_3, THEME_2
    );
}
