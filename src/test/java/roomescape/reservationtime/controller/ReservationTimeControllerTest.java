package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.hasItem;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/create_reservation_time.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeControllerTest {

    @Test
    void 예약_시간_목록을_조회한다() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("startAt", hasItem("10:00:00"));
    }

}
