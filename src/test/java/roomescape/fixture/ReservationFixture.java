package roomescape.fixture;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.domain.Reservation;

public class ReservationFixture {

    private static final String STARK = "스타크";
    private static final String KAYA = "카야";
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 5, 6);
    private static final String FUTURE_RESERVATION_DATE_TEXT = "2028-05-06";

    private ReservationFixture() {
    }

    public static ReservationCreateCommand starkCreateCommand(Long themeId, Long timeId) {
        return new ReservationCreateCommand(STARK, RESERVATION_DATE, themeId, timeId);
    }

    public static ReservationCreateCommand kayaCreateCommand(Long themeId, Long timeId) {
        return new ReservationCreateCommand(KAYA, RESERVATION_DATE, themeId, timeId);
    }

    public static Reservation starkReservation(Long themeId, Long timeId) {
        return Reservation.builder()
                .name(STARK)
                .date(RESERVATION_DATE)
                .themeId(themeId)
                .timeId(timeId)
                .build();
    }

    public static Map<String, String> futureReservationParams(Long themeId, Long timeId) {
        Map<String, String> params = new HashMap<>();
        params.put("name", STARK);
        params.put("date", FUTURE_RESERVATION_DATE_TEXT);
        params.put("themeId", String.valueOf(themeId));
        params.put("timeId", String.valueOf(timeId));
        return params;
    }
}