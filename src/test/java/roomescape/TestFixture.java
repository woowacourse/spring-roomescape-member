package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

public class TestFixture {
    public static String VALID_STRING_DATE = "9999-12-31";
    public static LocalDate DATE = LocalDate.parse(VALID_STRING_DATE);
    public static String VALID_STRING_TIME = "10:00";
    public static LocalTime TIME = LocalTime.parse(VALID_STRING_TIME);
    public static ReservationTime RESERVATION_TIME_10AM = new ReservationTime(TIME);
    public static ReservationTime RESERVATION_TIME_11AM = new ReservationTime(LocalTime.parse("11:00"));
    public static ReservationTime RESERVATION_TIME_12AM = new ReservationTime(LocalTime.parse("12:00"));
    public static RoomTheme ROOM_THEME1 = new RoomTheme("레벨 1 탈출",
            "우테코 레벨1를 탈출하는 내용입니다.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    public static RoomTheme ROOM_THEME2 = new RoomTheme("레벨 2 탈출",
            "우테코 레벨2를 탈출하는 내용입니다.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
}
