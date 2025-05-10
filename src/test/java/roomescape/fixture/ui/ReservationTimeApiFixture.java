package roomescape.fixture.ui;

import java.util.HashMap;
import java.util.Map;

public class ReservationTimeApiFixture {

    public static Map<String, String> reservationTimeParams1() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        return params;
    }

    public static Map<String, String> reservationTimeParams2() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "11:00");

        return params;
    }

    public static Map<String, String> reservationTimeParams3() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "14:00");

        return params;
    }
}
