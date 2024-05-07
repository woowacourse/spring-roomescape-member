package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ReservationTimeControllerIntegrationTest {

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void initReservation() {
        RestAssured.port = randomServerPort;
    }

    @DisplayName("전체 예약 시간 정보를 조회한다.")
    @Test
    void getReservationTimesTest() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(8));
    }

    @DisplayName("예약 시간 정보를 저장한다.")
    @Test
    void saveReservationTimeTest() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(9));
    }

    @DisplayName("존재하지 않는 예약 시간을 포함한 예약 저장 요청을 하면 400코드가 응답된다.")
    @Test
    void saveReservationWithNoExistReservationTime() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", "20");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 id의 예약 시간이 존재하지 않습니다."));
    }

    @DisplayName("현재 날짜보다 이전 날짜의 예약을 저장하려고 요청하면 400코드가 응답된다.")
    @Test
    void saveReservationWithReservationDateAndTimeBeforeNow() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약 일시는 현재 시간 이후여야 합니다."));
    }

    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        // 예약 시간 정보 삭제
        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(204);

        // 예약 시간 정보 조회
        final List<ReservationTimeResponse> reservationTimes = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimeResponse.class);

        assertThat(reservationTimes.size()).isEqualTo(7);
    }

    @DisplayName("존재하지 않는 예약 시간 정보를 삭제하려고 하면 400코드가 응답된다.")
    @Test
    void deleteNoExistReservationTimeTest() {
        RestAssured.given().log().all()
                .when().delete("/times/20")
                .then().log().all()
                .statusCode(400)
                .body("message", is("해당 id의 예약 시간이 존재하지 않습니다."));
    }
}
