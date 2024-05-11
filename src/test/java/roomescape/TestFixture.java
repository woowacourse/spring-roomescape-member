package roomescape;

import roomescape.domain.member.Member;
import roomescape.domain.member.Name;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;

public class TestFixture {
    public static final String MEMBER_MIA_NAME = "미아";
    public static final String MIA_RESERVATION_DATE = "2030-04-18";
    public static final String MIA_RESERVATION_TIME = "15:00";
    public static final String MEMBER_MIA_EMAIL = "mia@email.com";
    public static final String MEMBER_MIA_PASSWORD = "1234";

    public static final String MEMBER_TOMMY_NAME = "토미";
    public static final String TOMMY_RESERVATION_DATE = "2030-05-19";
    public static final String TOMMY_RESERVATION_TIME = "15:00";

    public static final String WOOTECO_THEME_NAME = "레벨2 탈출";
    public static final String WOOTECO_THEME_DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    public static final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
    public static final String HORROR_THEME_NAME = "호러";
    public static final String HORROR_THEME_DESCRIPTION = "매우 무섭습니다.";

    public static Reservation MIA_RESERVATION() {
        return MIA_RESERVATION(new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME());
    }

    public static Reservation MIA_RESERVATION(final ReservationTime time, final Theme theme) {
        return new Reservation(MEMBER_MIA(), MIA_RESERVATION_DATE, time, theme);
    }

    public static Reservation MIA_RESERVATION(final Member member, final ReservationTime time, final Theme theme) {
        return new Reservation(member, MIA_RESERVATION_DATE, time, theme);
    }

    public static Reservation TOMMY_RESERVATION() {
        return new Reservation(MEMBER_MIA(), TOMMY_RESERVATION_DATE,
                new ReservationTime(TOMMY_RESERVATION_TIME), WOOTECO_THEME());
    }

    public static Member MEMBER_MIA() {
        return new Member(new Name(MEMBER_MIA_NAME), MEMBER_MIA_EMAIL, MEMBER_MIA_PASSWORD, Role.USER);
    }

    public static Member MEMBER_MIA(final Long id) {
        return new Member(id, new Name(MEMBER_MIA_NAME), MEMBER_MIA_EMAIL, MEMBER_MIA_PASSWORD, Role.USER);
    }

    public static Theme WOOTECO_THEME() {
        return new Theme(WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION, THEME_THUMBNAIL);
    }

    public static Theme WOOTECO_THEME(final Long id) {
        return new Theme(id, WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION, THEME_THUMBNAIL);
    }

    public static Theme HORROR_THEME() {
        return new Theme(HORROR_THEME_NAME, HORROR_THEME_DESCRIPTION, THEME_THUMBNAIL);
    }

    public static Theme HORROR_THEME(final Long id) {
        return new Theme(id, HORROR_THEME_NAME, HORROR_THEME_DESCRIPTION, THEME_THUMBNAIL);
    }
}
