package roomescape;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationFlowTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_생성_후_해당_시간이_예약됨으로_변경된다() {
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");

        RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .body("[0].isReserved", equalTo(false));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-05-06");
        params.put("timeId", 1);
        params.put("themeId", 1);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes/1/times?date=2026-05-06")
                .then().log().all()
                .statusCode(200)
                .body("[0].isReserved", equalTo(true));
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update("INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertReservationTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }
}
