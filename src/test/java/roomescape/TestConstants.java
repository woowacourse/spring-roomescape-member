package roomescape;

import java.time.LocalTime;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

public class TestConstants {

    public static final ReservationTime DEFAULT_TIME = new ReservationTime(LocalTime.of(13, 5));
    public static final Theme DEFAULT_THEME = new Theme("탈출", "탈출하는 내용", "abc");
    public static final Member DEFAULT_MEMBER = new Member("두리", "a@a.com", "1234", Role.USER);
}
