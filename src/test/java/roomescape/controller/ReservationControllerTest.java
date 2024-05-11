package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Sql(value = {"/data-reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("예약 컨트롤러")
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("예약 컨트롤러는 예약 조회 시 값을 반환한다.")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @DisplayName("예약 컨트롤러는 예약 생성 시 생성된 값을 반환한다.")
    @Test
    void createReservation() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", LocalDate.MAX.toString());
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(8));
    }

    @DisplayName("예약 컨트롤러는 잘못된 형식의 날짜로 예약 생성 요청 시 400을 응답한다.")
    @ValueSource(strings = {"Hello", "2024-13-20", "2900-12-32"})
    @ParameterizedTest
    void createInvalidDateReservation(String invalidString) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", invalidString);
        reservation.put("timeId", 1);

        String detailMessage = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .extract()
                .jsonPath().get("detail");

        assertThat(detailMessage).isEqualTo("잘못된 형식의 날짜 혹은 시간입니다.");
    }

    @DisplayName("예약 컨트롤러는 예약 생성 시 잘못된 형식의 본문이 들어오면 400을 응답한다.")
    @Test
    void createInvalidRequestBody() {
        String invalidBody = "invalidBody";

        String detailMessage = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalidBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .extract()
                .jsonPath().get("detail");

        assertThat(detailMessage).isEqualTo("요청에 잘못된 형식의 값이 있습니다.");
    }

    @DisplayName("예약 컨트롤러는 id 값에 따라 예약을 삭제한다.")
    @Test
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(6));
    }
}
