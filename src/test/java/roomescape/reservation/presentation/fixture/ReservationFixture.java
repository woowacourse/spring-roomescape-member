package roomescape.reservation.presentation.fixture;

import java.util.HashMap;
import java.util.Map;

public class ReservationFixture {

    public Map<String, String> createReservation(String name, String date, String themeId, String timeId) {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", name);
        reservationParams.put("date", date);
        reservationParams.put("themeId", themeId);
        reservationParams.put("timeId", timeId);
        return reservationParams;
    }

    public Map<String, String> createReservationTime(String startAt) {
        Map<String, String> reservationTimeParams = new HashMap<>();
        reservationTimeParams.put("startAt", startAt);
        return reservationTimeParams;
    }

    public Map<String, String> createTheme(String name, String description, String thumbnail) {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", name);
        themeParams.put("description", description);
        themeParams.put("thumbnail", thumbnail);
        return themeParams;
    }
}
