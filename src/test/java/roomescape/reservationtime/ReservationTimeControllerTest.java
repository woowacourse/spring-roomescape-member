package roomescape.reservationtime;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "14:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));

        RestAssured.given().log().all()
                .when().delete("/times/5")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 특정날짜와_테마에_예약_가능_시간들_조회_API() {
        Map<String, Object> options = new HashMap<>();
        options.put("date", "2026-05-05");
        options.put("themeId", 1);

        RestAssured.given().log().all()
                .params(options)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(1))
                .body("[0].isAvailable", is(false))
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 및 예약 생성 이후 예약 가능 시간을 재조회를 할 수 있다.")
    void 정상_흐름_테스트() {
        Map<String, Object> options = new HashMap<>();
        options.put("date", "2026-05-05");
        options.put("themeId", 4);

        // 2026-05-05 4번 테마 조회
        RestAssured.given().log().all()
                .params(options)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(1))
                .body("[0].isAvailable", is(true))
                .statusCode(200);

        // 예약 생성
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-05");
        reservation.put("timeId", 4);
        reservation.put("themeId", 4);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // 특정날짜와_테마에_예약_가능_시간을_조회
        Map<String, Object> options1 = new HashMap<>();
        options1.put("date", "2026-05-05");
        options1.put("themeId", 4);

        RestAssured.given().log().all()
                .params(options1)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(1))
                .body("[0].isAvailable", is(false))
                .statusCode(200);
    }
}
