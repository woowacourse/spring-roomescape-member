package roomescape.controller.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {
    @DisplayName("이미 예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void deleteTimeExistReservation() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200);

        LocalDate now = LocalDate.now();
        Map<String, Object> reservationParams = Map.of(
                "name", "test",
                "date", now.plusDays(1).toString(),
                "timeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
