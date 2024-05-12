package roomescape.reservation.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.base.BaseTest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.SearchRequest;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest extends BaseTest {

    @Test
    void findReservationListTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(25));
    }

    @Test
    void findReservationListBySearchInfo() {
        SearchRequest searchRequest = new SearchRequest(1L, 1L,
                LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 6));

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(searchRequest)
                .when()
                .post("/reservations/search")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @Test
    void saveReservationTest() {
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(3000, 1, 1), 1L, 1L);

        RestAssured.given()
                .cookie("token", memberToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .log().all()
                .when()
                .post("reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deleteReservationByIdTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("reservations/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
