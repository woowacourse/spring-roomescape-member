package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/recreate_table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("어드민 예약 컨트롤러")
class AdminReservationControllerTest {

    @LocalServerPort
    private int port;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        Map<String, String> body = new HashMap<>();
        body.put("email", "admin@gmail.com");
        body.put("password", "password");

        accessToken = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header(HttpHeaders.SET_COOKIE)
                .split(";")[0]
                .split("token=")[1];
    }

    @DisplayName("어드민 예약 컨트롤러는 예약 조회 시 값을 반환한다.")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(9));
    }

    @DisplayName("어드민 예약 컨트롤러는 기간, 사용자, 테마로 예약 조회 시 값을 반환한다.")
    @Test
    void searchReservations() {
        RestAssured.given().log().all()
                .queryParam("dateFrom", "2024-01-01")
                .queryParam("dateTo", "2099-12-31")
                .queryParam("memberId", 1)
                .queryParam("themeId", 2)
                .cookie("token", accessToken)
                .when().get("/admin/reservations/search")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("어드민 예약 컨트롤러는 예약 생성 시 생성된 값을 반환한다.")
    @Test
    void createReservation() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", 1);
        reservation.put("date", LocalDate.MAX.toString());
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .when().get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(9));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .when().get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(10));
    }

    @DisplayName("어드민 예약 컨트롤러는 id 값에 따라 예약을 삭제한다.")
    @Test
    void deleteReservation() {
        RestAssured.given()
                .cookie("token", accessToken)
                .when().get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(9));

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given()
                .cookie("token", accessToken)
                .when().get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(8));
    }
}
