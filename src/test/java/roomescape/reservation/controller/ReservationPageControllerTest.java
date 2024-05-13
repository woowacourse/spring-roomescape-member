package roomescape.reservation.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.base.BaseTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationPageControllerTest extends BaseTest {

    @Test
    void reservationTest() {
        RestAssured.given()
                .cookie(memberToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/reservation")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
