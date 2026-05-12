package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationFlowTest {

    @Test
    void 예약_추가시_가능_시간_제외() {
        int themeId = 2;
        String date = "2026-12-31";

        int sizeBefore = RestAssured.given().log().all()
                .queryParam("theme_id", themeId)
                .queryParam("date", date)
                .when().get("/api/times/availability")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getInt("size()");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "동키");
        reservation.put("themeId", themeId);
        reservation.put("date", date);
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .queryParam("theme_id", themeId)
                .queryParam("date", date)
                .when().get("/api/times/availability")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(sizeBefore - 1));
    }

    @Test
    void 중복_예약_추가시_409() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "매트");
        reservation.put("themeId", 1);
        reservation.put("date", "2026-12-30");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 과거_시간_예약시_422() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "동키");
        reservation.put("themeId", 1);
        reservation.put("date", "2024-01-01");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(422);
    }
}
