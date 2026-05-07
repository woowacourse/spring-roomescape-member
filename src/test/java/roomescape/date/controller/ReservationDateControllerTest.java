package roomescape.date.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
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
class ReservationDateControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 예약 가능한 날짜 목록을 조회한다.")
    void getReservationDates() {
        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("사용자는 오늘 이후의 예약 날짜 목록을 조회한다.")
    void getReservationDatesAfterToday() {
        String tomorrow = LocalDate.now().plusDays(1).toString();

        Map<String, String> params = new HashMap<>();
        params.put("date", tomorrow);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].date", is(tomorrow));
    }

    @Test
    @DisplayName("사용자는 오늘 이전의 예약 날짜는 조회할 수 없다.")
    void getReservationDatesExcludePastDates() {
        String yesterday = LocalDate.now().minusDays(1).toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();

        Map<String, String> pastDate = new HashMap<>();
        pastDate.put("date", yesterday);

        Map<String, String> futureDate = new HashMap<>();
        futureDate.put("date", tomorrow);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(pastDate)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(400);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(futureDate)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("date", contains(tomorrow));
    }

}
