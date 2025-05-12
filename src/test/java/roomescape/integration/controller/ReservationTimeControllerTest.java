package roomescape.integration.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.controller.ReservationTimeController;
import roomescape.integration.service.stub.StubReservationTimeService;

/**
 * 해당 클래스는 ReservationTimeController 의 요청/응답 형식을 테스트합니다.
 * SpringBootTest 어노테이션을 사용하여 실제로 서버를 띄우는 통합 테스트입니다.
 * 단, StubReservationTimeService 를 사용하여 프로덕션 Service 와 분리했고, 실제 DB에 접근하지 않고 테스트합니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
    void readAllReservationTime() {
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
    void readAllAvailableTimesBy() {
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
