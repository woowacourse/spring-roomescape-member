package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationControllerTest extends ControllerTest {

    @DisplayName("사용자 예약 추가")
    @Test
    void 사용자_예약_추가_API() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("사용자 예약 삭제")
    @Test
    void 사용자_예약_삭제() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("사용자 예약 조회")
    @Test
    void 사용자_예약_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("username", "김철수")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("존재하지 않는 시간으로 예약하면 400")
    @Test
    void 존재하지_않는_시간으로_예약하면_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 999);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("존재하지 않는 예약 시간입니다."));
    }

    @DisplayName("존재하지 않는 테마로 예약하면 400")
    @Test
    void 존재하지_않는_테마로_예약하면_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("존재하지 않는 테마입니다."));
    }

    @DisplayName("이미 예약된 시간이면 400")
    @Test
    void 이미_예약된_시간이면_400() {
        // data.sql 첫 번째 예약: 2026-04-29, time_id=3, theme_id=1
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-04-29");
        params.put("timeId", 3);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("이미 예약된 시간입니다."));
    }

    @DisplayName("과거 날짜로 예약하면 400")
    @Test
    void 과거_날짜로_예약하면_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("과거 날짜로는 예약할 수 없습니다."));
    }
}
