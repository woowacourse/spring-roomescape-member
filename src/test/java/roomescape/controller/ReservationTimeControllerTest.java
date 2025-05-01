package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.service.stub.StubReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationTimeControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ReservationTimeController reservationTimeController() {
            return new ReservationTimeController(new StubReservationTimeService());
        }
    }

    @Test
    void createReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(params)
                .when().post("/times")
                .then().log().all()
                .body("startAt", equalTo("12:00:00"))
                .statusCode(201);
    }

    @Test
    void readReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .body("[0].startAt", equalTo("12:00:00"))
                .statusCode(200);
    }

    @Test
    void deleteReservationTime() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void readAvailableTimesBy() {
        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-05-01");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(params)
                .when().get("times/search")
                .then().log().all()
                .body("[0].startAt", equalTo("12:00:00"))
                .body("[0].booked", equalTo(false))
                .statusCode(200);
    }
}
