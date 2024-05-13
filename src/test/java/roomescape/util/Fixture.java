package roomescape.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

public final class Fixture {
    public static final String JOJO_NAME = "jojo";
    public static final String JOJO_EMAIL = "jojo@gmail.com";
    public static final String JOJO_PASSWORD = "12345";
    public static final Member MEMBER_JOJO = new Member(JOJO_NAME, JOJO_EMAIL, JOJO_PASSWORD, Role.MEMBER);

    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "12345";

    public static final String MEMBER_EMAIL = "imjojo@gmail.com";
    public static final String MEMBER_PASSWORD = "qwer";

    public static final String START_AT_16_00 = "16:00";
    public static final ReservationTime RESERVATION_TIME_16_00 = new ReservationTime(START_AT_16_00);

    public static final String LEVEL2_NAME = "레벨2 탈출";
    public static final String LEVEL2_DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    public static final String LEVEL2_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
    public static final Theme THEME_LEVEL2 = new Theme(LEVEL2_NAME, LEVEL2_DESCRIPTION, LEVEL2_THUMBNAIL);

    public static final Long ID_1 = 1L;
    public static final String DATE_2024_05_06 = "2024-05-06";

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

    private Fixture() {
    }
}
