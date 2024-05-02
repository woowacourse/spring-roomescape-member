package roomescape.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReservationTimeIntegrationTest extends IntegrationTest {
    @Test
    void 시간_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약이_가능한_시간을_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/times/available?date=2024-10-05&theme-id=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약이_불가한_시간을_필터링해_조회할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/times/available?date=2024-08-05&theme-id=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1)); // TODO: body의 필드 검증하기
    }

    @Test
    void 시간을_추가할_수_있다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "11:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/2")
                .body("id", is(2));
    }

    @Test
    void 시작_시간이_빈_값이면_시간을_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 시작_시간의_형식이_다르면_시간을_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "25:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 중복된_시간은_추가할_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void 시간을_삭제할_수_있다() {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", 1);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(countAfterDelete).isZero();
    }

    @Test
    void 존재하지_않는_시간은_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/times/13")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_시간은_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
