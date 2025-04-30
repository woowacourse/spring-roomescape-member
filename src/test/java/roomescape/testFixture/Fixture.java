package roomescape.testFixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class Fixture {
    public static final Theme THEME_1 = Theme.of(1L, "테마1", "테마 1입니다.", "썸네일");
    public static final ReservationTime RESERVATION_TIME_1 = ReservationTime.of(1L, LocalTime.of(10, 0));
    public static final ReservationTime RESERVATION_TIME_2 = ReservationTime.of(2L, LocalTime.of(11, 0));
    public static final ReservationTime RESERVATION_TIME_3 = ReservationTime.of(3L, LocalTime.of(12, 0));
    public static final Reservation RESERVATION_1 = Reservation.of(1L, "이름", THEME_1, LocalDate.now().plusDays(1),
            RESERVATION_TIME_1);

    public static final Map<String, Object> RESERVATION_BODY = createReservationBody();

    private static Map<String, Object> createReservationBody() {
        Map<String, Object> params = new HashMap<>();

        params.put("name", "브라운");
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        return params;
    }
}
