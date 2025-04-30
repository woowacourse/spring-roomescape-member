package roomescape.testFixture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import roomescape.domain.Theme;

public class Fixture {
    public static final Theme THEME_1 = Theme.of(1L, "테마1", "테마 1입니다.", "썸네일");

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
