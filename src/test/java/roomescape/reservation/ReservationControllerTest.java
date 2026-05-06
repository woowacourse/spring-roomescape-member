package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {
    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/api/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(17));
    }

    @Test
    void 예약_추가() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "매트");
        reservation.put("themeId", "1");
        reservation.put("date", "2026-05-10");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 예약_삭제() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 빈값으로_예약_추가시_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", "2023-08-05");
        params.put("time_id", 0);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_예약_삭제시_404() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/0")
                .then().log().all()
                .statusCode(404);
    }
}
