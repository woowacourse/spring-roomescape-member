package roomescape.reservation.presentation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    @Test
    @DisplayName("예약 생성시 비어있는 body를 보내면 400 Bad Request가 발생한다.")
    void createReservation_MissingParameter_Throws400() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
