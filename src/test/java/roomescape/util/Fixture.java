package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

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

    public static final String YESTERDAY = LocalDate.now()
            .minusDays(1)
            .format(DateTimeFormatter.ISO_DATE);

    public static final String TODAY = LocalDate.now()
            .format(DateTimeFormatter.ISO_DATE);

    public static final String TOMORROW = LocalDate.now()
            .plusDays(1)
            .format(DateTimeFormatter.ISO_DATE);

    public static final String PAST_TIME = LocalTime.now()
            .minusSeconds(1)
            .format(DateTimeFormatter.ofPattern("HH:mm"));

    public Fixture() {
    }
}
