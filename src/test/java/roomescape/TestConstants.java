package roomescape;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class TestConstants {

    public static final ReservationTime DEFAULT_TIME = new ReservationTime(LocalTime.of(13, 5));
    public static final Theme DEFAULT_THEME = new Theme("탈출", "탈출하는 내용", "abc");
}
