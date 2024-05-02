package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deletedReservationNotFound() {
        RestAssured.given().log().all()
                .when().delete("/reservations/100")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservationsWhenEmpty() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(13));
    }

    @DisplayName("예약 추가 및 삭제")
    @Test
    void saveAndDeleteReservation() {
        final Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2025-08-05",
                "timeId", 1L,
                "themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/14");

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 시간으로 예약 추가")
    @Test
    void reservationTimeForSaveNotFound() {
        final Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2025-08-05",
                "timeId", 100L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("유효하지 않은 날짜 형식 입력")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", "2022.22.11"})
    void invalidTimeFormat(final String time) {
        final Map<String, String> params = Map.of("startAt", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
