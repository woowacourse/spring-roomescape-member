package roomescape.domain.time.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAllReservationTimes() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(6));
    }

    @Test
    @DisplayName("예약 시간을 생성한다.")
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
    @DisplayName("예약 시간 생성 시 시간이 null이면 요청을 거부한다.")
    void createReservationTime_throwsException_whenStartAtIsNull() {
        String params = """
                {
                    "startAt": null
                }
                """;

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("유효하지 않은 요청입니다."))
                .body("details.size()", is(1))
                .body("details[0].field", is("startAt"))
                .body("details[0].message", is("예약 시간은 필수입니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "99:99"})
    @DisplayName("예약 시간 생성 시 시간 형식이 잘못되면 요청을 거부한다.")
    void createReservationTime_throwsException_whenStartAtFormatInvalid(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST_BODY"))
                .body("message", is("요청 값이 올바르지 않습니다."));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
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
    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다.")
    void deleteReservationTime_throwsException_whenReservationExists() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("code", is("RESERVATION_TIME_DELETE_CONFLICT"))
                .body("message", is("예약이 존재하는 시간은 삭제할 수 없습니다."));
    }
}
