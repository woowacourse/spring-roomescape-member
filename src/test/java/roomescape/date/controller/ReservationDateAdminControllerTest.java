package roomescape.date.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDateAdminControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private final String date = LocalDate.of(2099, 1, 1).toString();


    @Test
    @DisplayName("예약 날짜 목록을 조회한다.")
    void getReservationDates() {
        RestAssured.given().log().all()
                .when().get("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 날짜를 생성한다.")
    void createReservationDate() {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .body("date", is(date));

        RestAssured.given().log().all()
                .when().get("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 날짜를 삭제한다.")
    void deleteReservationDate() {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().delete("/admin/dates/1")
                .then().log().all()
                .statusCode(200)
                .body("date", is(date));

        RestAssured.given().log().all()
                .when().get("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 날짜를 생성한 뒤 조회한다.")
    void createAndGetReservationDates() {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("date가 없으면 예약 날짜 생성에 실패한다.")
    void createReservationDateWithoutDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(400);
    }

}
