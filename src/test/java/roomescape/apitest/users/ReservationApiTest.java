package roomescape.apitest.users;

import static org.hamcrest.Matchers.is;
import static roomescape.config.FixedClockConfig.FUTURE_DATE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @Test
    void 예약_사용자_API() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", FUTURE_DATE);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(25));
    }

    @Test
    @DisplayName("사용자 이름이 null이면 상태코드 400을 반환한다.")
    void 요청_이름_null_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", FUTURE_DATE);
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜가 null이면 상태코드 400을 반환한다.")
    void 요청_날짜_null_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜의 형식이 올바르지 않으면 상태코드 400을 반환한다.")
    void 요청_날짜_형식_불일치_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "26-01-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시간 식별자가 null이면 상태코드 400을 반환한다.")
    void 요청_시간_식별자_null_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", FUTURE_DATE);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("테마 식별자가 null이면 상태코드 400을 반환한다.")
    void 요청_테마_식별자_null_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", FUTURE_DATE);
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
