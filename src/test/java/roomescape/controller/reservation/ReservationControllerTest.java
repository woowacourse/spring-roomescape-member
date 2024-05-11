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
    @DisplayName("여러 조건으로 예약을 조회할 경우 200 과 조건에 부합하는 예약 리스트를 응답한다.")
    void getReservationsConditions200Reservations() {
        String twoDaysAgo = LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

        RestAssured.given().log().all()
                .contentType(ContentType.URLENC)
                .param("themeId", 3)
                .param("memberId", 2)
                .param("dateFrom", twoDaysAgo)
                .param("dateTo", yesterday)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("토큰 없이 요청할 경우 401 을 반환한다.")
    void reqeustWithoutToken() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(tomorrow, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401)
                .body("message", containsString("토큰"));
    }

    @Test
    @DisplayName("예약을 추가 하면 201 과 예약 정보를 응답한다.")
    void addReservation201AndReservation() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(tomorrow, 1L, 1L);

        RestAssured.given().log().all()
                .cookie(token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/reservations/"))
                .body("date", is(request.date()))
                .body("time.startAt", is("08:00"))
                .body("theme.name", is("젠틀 먼데이"))
                .body("member.name", is("새양"));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약을 추가하면 404 을 응답한다.")
    void addReservation404TimeNotFound() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(tomorrow, 0L, 1L);

        RestAssured.given().log().all()
                .cookie(token)
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
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(tomorrow, 1L, 0L);

        RestAssured.given().log().all()
                .cookie(token)
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
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(tomorrow, 2L, 2L);

        RestAssured.given().log().all()
                .cookie(token)
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
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        CreateReservationRequest request = CreateReservationRequest.from(yesterday, 2L, 2L);

        RestAssured.given().log().all()
                .cookie(token)
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
