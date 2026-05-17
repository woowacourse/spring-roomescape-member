package roomescape.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약 시간을 생성할 수 있다.")
    void 예약_시간_생성_성공() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", is("10:00:00"));
    }

    @Test
    @DisplayName("시간이 없는 경우 예약 시간을 생성할 수 없다.")
    void 예약_시간_생성_실패_시간_없음() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 시간인 경우 예약 시간을 생성할 수 없다.")
    void 예약_시간_생성_실패_중복_시간() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("예약 시간이 없으면 목록 조회 시 빈 결과를 반환한다.")
    void 예약_시간_없을때_목록_조회() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 시간 목록을 조회할 수 있다.")
    void 예약_시간_목록_조회() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00')");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약 시간을 수정할 수 있다.")
    void 예약_시간_수정_성공() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/times/1")
                .then().log().all()
                .statusCode(200)
                .body("startAt", is("12:00:00"));
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간인 경우 수정할 수 없다.")
    void 예약_시간_수정_실패_존재하지_않는_시간() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다.")
    void 예약_시간_삭제_성공() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간인 경우 삭제할 수 없다.")
    void 예약_시간_삭제_실패_존재하지_않는_시간() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약이 있는 시간인 경우 삭제할 수 없다.")
    void 예약_시간_삭제_실패_예약이_있는_시간() {
        jdbcTemplate.update("INSERT INTO theme (name, description, url) VALUES ('무서운 이야기', '공포', 'http://example.com')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");
        jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id) VALUES ('브라운', '2026-08-04', 1, 1)");

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409);
    }
}
