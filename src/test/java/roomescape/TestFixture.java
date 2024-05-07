package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public class TestFixture {
    public static LocalDate DATE_FIXTURE = LocalDate.of(9999, 12, 31);
    public static LocalTime TIME_FIXTURE = LocalTime.of(10, 0);
    public static ReservationTime RESERVATION_TIME_FIXTURE = new ReservationTime(TIME_FIXTURE);
    public static RoomTheme ROOM_THEME_FIXTURE = new RoomTheme("레벨 2 탈출",
            "우테코 레벨2를 탈출하는 내용입니다.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
}
