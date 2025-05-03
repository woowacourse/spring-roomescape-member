package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.service.stub.StubReservationService;
import roomescape.service.stub.StubReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ReservationController reservationController() {
            return new ReservationController(new StubReservationService());
        }
    }

    @Test
    void createReservation() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "히스타");
        params.put("date", "2025-05-01");
        params.put("themeId", "1");
        params.put("timeId", "1");

        RestAssured.given().log().all()
                .contentType("application/json")
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .body("name", equalTo("히스타"))
                .body("date", equalTo("2025-05-01"))
                .body("theme.id", equalTo(1))
                .body("time.id", equalTo(1))
                .body("time.startAt", equalTo("12:00:00"))
                .body("theme.name", equalTo("name"))
                .body("theme.description", equalTo("description"))
                .body("theme.thumbnail", equalTo("thumbnail"))
                .statusCode(201);
    }

    @Test
    void readReservation() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .body("[0].name", equalTo("히스타"))
                .body("[0].date", equalTo("2025-05-01"))
                .body("[0].theme.id", equalTo(1))
                .body("[0].time.id", equalTo(1))
                .body("[0].time.startAt", equalTo("12:00:00"))
                .body("[0].theme.name", equalTo("name"))
                .body("[0].theme.description", equalTo("description"))
                .body("[0].theme.thumbnail", equalTo("thumbnail"))
                .statusCode(200);
    }

    @Test
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
