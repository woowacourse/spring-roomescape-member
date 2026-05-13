package roomescape.domain.reservationtime.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 예약 시간을 생성한다.")
    void createReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("startAt", is("10:00"));
    }

    @Test
    @DisplayName("관리자는 예약 시간을 삭제한다.")
    void deleteReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("관리자라도 예약이 존재하는 시간을 삭제하면 에러가 발생한다.")
    void deleteReservationTimeWithReservationThrowException() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("관리자라도 유효하지 않은 입력값으로 예약 시간을 생성하면 에러가 발생한다.")
    void createReservationTimeWithInvalidInputThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("관리자는 예약 시간을 수정한다.")
    void updateReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "16:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/times/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("startAt", is("16:00"));
    }

    @Test
    @DisplayName("관리자라도 존재하지 않는 예약 시간을 수정하면 에러가 발생한다.")
    void updateReservationTimeWithNotFoundThrowException() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("관리자라도 존재하지 않는 예약 시간을 삭제하면 에러가 발생한다.")
    void deleteReservationTimeWithNotFoundThrowException() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }
}
