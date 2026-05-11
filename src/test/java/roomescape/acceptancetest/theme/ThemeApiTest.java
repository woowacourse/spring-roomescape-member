package roomescape.acceptancetest.theme;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
public class ThemeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 인기_테마_API() {
        AcceptanceTestFixture.createTheme("미술관의 밤");
        AcceptanceTestFixture.createTheme("사라진 열쇠");
        AcceptanceTestFixture.createTheme("비밀의 방");
        AcceptanceTestFixture.createReservationTime("10:00", 1L);
        AcceptanceTestFixture.createReservationTime("11:00", 1L);
        AcceptanceTestFixture.createReservationTime("12:00", 2L);
        AcceptanceTestFixture.createReservationTime("13:00", 3L);

        AcceptanceTestFixture.insertReservation(jdbcTemplate, "브라운", AcceptanceTestFixture.today().minusDays(1), 1L);
        AcceptanceTestFixture.insertReservation(jdbcTemplate, "코니", AcceptanceTestFixture.today().minusDays(1), 2L);
        AcceptanceTestFixture.insertReservation(jdbcTemplate, "샐리", AcceptanceTestFixture.today().minusDays(7), 3L);
        AcceptanceTestFixture.insertReservation(jdbcTemplate, "문", AcceptanceTestFixture.today().minusDays(8), 4L);

        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("id", contains(1, 2))
                .body("name", contains("미술관의 밤", "사라진 열쇠"));
    }

}
