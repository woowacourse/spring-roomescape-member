package roomescape.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequest;
import roomescape.dto.ReservationRequest;

@Sql("/reservation-api-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationApiTest {
    @LocalServerPort
    int port;
    Cookie adminCookie;

    @BeforeEach
    void 쿠키_생성() {
        LoginRequest loginRequest = new LoginRequest("admin@admin.com", "adminadmin");
        adminCookie = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/login")
                .getDetailedCookie("token");
    }

    @Test
    void 예약_추가() {
        ReservationRequest reservationRequest = createReservationRequest();

        RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .cookie(adminCookie)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/1")
                .body("id", equalTo(1))
                .body("date", equalTo(reservationRequest.date().toString()))
                .body("time.id", equalTo(reservationRequest.timeId().intValue()))
                .body("theme.id", equalTo(reservationRequest.themeId().intValue()));
    }

    @Test
    void 예약_단일_조회() {
        ReservationRequest reservationRequest = createReservationRequest();

        addReservation(reservationRequest);

        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("date", equalTo(reservationRequest.date().toString()))
                .body("time.id", equalTo((reservationRequest.timeId().intValue())))
                .body("theme.id", equalTo(reservationRequest.themeId().intValue()));
    }

    @Test
    void 예약_전체_조회() {
        ReservationRequest reservationRequest = createReservationRequest();

        addReservation(reservationRequest);

        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 예약_삭제() {
        ReservationRequest reservationRequest = createReservationRequest();

        addReservation(reservationRequest);

        given().log().all()
                .port(port)
                .cookie(adminCookie)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    private ReservationRequest createReservationRequest() {
        return new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);
    }

    private void addReservation(final ReservationRequest reservationRequest) {
        RestAssured.given().log().all()
                .port(port)
                .cookie(adminCookie)
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations");
    }
}
