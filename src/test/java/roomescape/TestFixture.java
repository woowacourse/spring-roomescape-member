package roomescape;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class TestFixture {
    public static final String USER_MIA = "미아";
    public static final String MIA_RESERVATION_DATE = "2030-04-18";
    public static final String MIA_RESERVATION_TIME = "15:00";

    public static final String USER_TOMMY = "토미";
    public static final String TOMMY_RESERVATION_DATE = "2030-05-19";
    public static final String TOMMY_RESERVATION_TIME = "15:00";

    public static final String THEME_NAME = "레벨2 탈출";
    public static final String THEME_DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    public static final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    public static Reservation MIA_RESERVATION() {
        return MIA_RESERVATION(new ReservationTime(MIA_RESERVATION_TIME));
    }

    public static Reservation MIA_RESERVATION(ReservationTime time) {
        return new Reservation(USER_MIA, MIA_RESERVATION_DATE, time);
    }

    public static Reservation TOMMY_RESERVATION() {
        return new Reservation(USER_TOMMY, TOMMY_RESERVATION_DATE, new ReservationTime(TOMMY_RESERVATION_TIME));
    }

    public static Theme WOOTECO_THEME() {
        return new Theme(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
    }

    public static Theme WOOTECO_THEME(Long id) {
        return new Theme(id, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
    }
}
