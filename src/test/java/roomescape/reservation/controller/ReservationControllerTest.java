package roomescape.reservation.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @DisplayName("방탈출 예약 추가 API를 테스트합니다.")
    @Test
    void save_reservation() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "스타크");
        params.put("date", "2026-05-06");
        params.put("themeId", "1");
        params.put("timeId", "1");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("name", equalTo("스타크"))
                .body("date", equalTo("2026-05-06"))
                .body("time.id", equalTo(1))
                .body("time.startAt", equalTo("09:00"))
                .body("theme.id", equalTo(1))
                .body("theme.name", equalTo("추리 탐정사무소"));
    }
}
