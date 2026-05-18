package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.exception.ErrorMessage;

public class ReservationTimeAcceptanceTest extends AcceptanceTestSupport{

    @Test
    @DisplayName("예약 시간 생성 요청에 시작 시간이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullStartTimeReservationTimeRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is("시작 시간은 필수입니다."));
    }

    @Test
    @DisplayName("이미 등록된 시작 시간으로 생성 요청 시 409 상태코드를 반환한다.")
    void duplicateStartAtReservationTimeRequestTest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorMessage.DUPLICATE_TIME.getMessage()));
    }

    @Test
    @DisplayName("존재하지 않는 시간 삭제 요청 시 404 상태코드를 반환한다.")
    void deleteNotExistTimeRequestTest() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/999")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("예약이 존재하는 시간 삭제 요청 시 409 상태코드를 반환한다.")
    void deleteTimeInUseRequestTest() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'http://localhost/thumbnail', '공포_설명', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, 'user_a', '2026-06-01', 1, 1)");

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(ErrorMessage.TIME_IN_USE.getMessage()));
    }

    @Test
    @DisplayName("예약 시간 생성 요청에 형식이 맞지 않은 시간이 입력된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void invalidFormatReservationTimeRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "11:00:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is(ErrorMessage.INVALID_DATA_FORMAT.getMessage()));
    }
}
