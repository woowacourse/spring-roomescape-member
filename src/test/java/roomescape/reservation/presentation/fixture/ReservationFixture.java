package roomescape.reservation.presentation.fixture;

import java.util.HashMap;
import java.util.Map;

public class ReservationFixture {

    public Map<String, String> createReservationTime(String startAt){
        Map<String, String> reservationTimeParams = new HashMap<>();
        reservationTimeParams.put("startAt", startAt);
        return reservationTimeParams;
    }

    public Map<String, String> createReservation(String name, String date, String timeId){
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", name);
        reservationParams.put("date", date);
        reservationParams.put("timeId", timeId);
        return reservationParams;
    }
}
