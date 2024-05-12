package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;

@Sql("/reservation.sql")
class AdminAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/admin/reservations";

    @Test
    void 필터링된_예약을_조회한다() {
        Map<String, String> params = Map.of(
                "themeId", "3",
                "memberId", "2",
                "dateFrom", "2024-04-26",
                "dateTo", "2024-05-01"
        );
        SimpleRestAssured.get(PATH, adminToken(), params)
                .statusCode(200)
                .body("size()", is(3));
    }
}
