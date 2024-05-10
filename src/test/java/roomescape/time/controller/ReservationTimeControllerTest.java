package roomescape.time.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.time.dto.ReservationTimeRequest;

import java.time.LocalTime;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findAllTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/times")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(10));
    }

    @Test
    void saveTest() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationTimeRequest)
                .log().all()
                .when()
                .post("times")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deleteTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("times/5")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
