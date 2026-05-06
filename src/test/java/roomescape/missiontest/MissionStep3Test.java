package roomescape.missiontest;

import static org.hamcrest.Matchers.is;

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
public class MissionStep3Test {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약과_시간_연결() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
