package roomescape.controller.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.login.TokenRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Sql(value = "/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    final TokenRequest request = new TokenRequest("seyang@test.com", "seyang");

    @Autowired
    ReservationController reservationController;
    @LocalServerPort
    int port;

    Cookie token;

    @BeforeEach
    void setUpEach() {
        RestAssured.port = port;
        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract()
                .detailedCookie("token");
    }

    @Test
    @DisplayName("예약 목록을 조회하면 200 과 예약 리스트를 응답한다.")
    void getReservations200AndReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(8));
    }

    @Test
    @DisplayName("예약을 추가하면 201 과 예약 정보를 응답한다.")
    void addReservation201AndReservation() {
        final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(tomorrow, 1L, 1L, 2L);

        RestAssured.given().log().all()
                .cookie(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/reservations/"))
                .body("name", is("새양"))
                .body("date", is(request.date()))
                .body("time.startAt", is("08:00"))
                .body("theme.name", is("젠틀 먼데이"));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약을 추가하면 404 을 응답한다.")
    void addReservation404TimeNotFound() {
        final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(tomorrow, 0L, 1L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("message", containsString("시간"))
                .body("message", containsString("존재"));
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약을 추가하면 404 을 응답한다.")
    void addReservation400ThemeNotFound() {
        final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(tomorrow, 1L, 0L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("message", containsString("테마"))
                .body("message", containsString("존재"));
    }

    @Test
    @DisplayName("이미 예약이 된 테마와 날짜 및 시간으로 예약을 추가하면 409을 응답한다.")
    void addReservation400Duplicated() {
        final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(tomorrow, 2L, 2L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("message", containsString("중복"));
    }

    @Test
    @DisplayName("지난 시간으로 예약을 추가하면 400 을 응답한다.")
    void aadReservation400PreviousTime() {
        final String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        final ReservationRequest request = new ReservationRequest(yesterday, 2L, 2L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", containsString("지난"));
    }

    @Test
    @DisplayName("존재하는 예약을 삭제하면 204 를 응답한다.")
    void deleteReservations204() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 404 를 응답한다.")
    void deleteReservations404() {
        RestAssured.given().log().all()
                .when().delete("/reservations/0")
                .then().log().all()
                .statusCode(404);
    }
}
