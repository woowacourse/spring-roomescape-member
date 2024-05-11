package roomescape.util;

import roomescape.core.domain.Member;
import roomescape.core.domain.ReservationTime;
import roomescape.core.domain.Role;
import roomescape.core.domain.Theme;

public class Fixture {
    public static final String MEMBER_NAME = "jojo";
    public static final String MEMBER_EMAIL = "jojo@gmail.com";
    public static final String MEMBER_PASSWORD = "12345";
    public static final Member MEMBER = new Member(MEMBER_NAME, MEMBER_EMAIL, MEMBER_PASSWORD, Role.MEMBER);

    public static final String START_AT = "16:00";
    public static final ReservationTime RESERVATION_TIME = new ReservationTime(START_AT);

    public static final String THEME_NAME = "레벨2 탈출";
    public static final String THEME_DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    public static final String THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
    public static final Theme THEME = new Theme(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

    public static final Long ID = 1L;
    public static final String DATE = "2024-05-06";

    public Fixture() {
    }
}
