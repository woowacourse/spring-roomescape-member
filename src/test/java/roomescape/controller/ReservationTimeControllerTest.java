package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Sql(value = {"/recreateReservationTime.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("시간 컨트롤러")
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("시간 컨트롤러는 시간 추가 요청이 들어오면 저장 후 200을 반환한다.")
    @Test
    void createTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("시간 컨트롤러는 잘못된 형식의 시간으로 시간 생성 요청 시 400을 응답한다.")
    @ValueSource(strings = {"aaa", "10:000", "25:30"})
    @ParameterizedTest
    void createInvalidTimeReservationTime(String invalidTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", invalidTime);

        String detailMessage = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .extract()
                .jsonPath().get("detail");

        assertThat(detailMessage).isEqualTo("잘못된 형식의 날짜 혹은 시간입니다.");
    }

    @DisplayName("시간 컨트롤러는 시간 조회 요청이 들어오면 저장된 시간을 반환한다.")
    @Test
    void readTimes() {
        createInitReservationTime();

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("시간 컨트롤러는 시간 삭제 요청이 들어오면 삭제 후 200을 반환한다.")
    @Test
    void deleteTime() {
        createInitReservationTime();

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    private void createInitReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(200);
    }
}
