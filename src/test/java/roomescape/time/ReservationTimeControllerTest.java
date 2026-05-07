package roomescape.time;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Test
    void 예약시간_조회() {
        RestAssured.given().log().all()
                .when().get("/api/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    void 예약_가능한_시간_조회() {
        RestAssured.given().log().all()
                .queryParam("theme_id", 1)
                .queryParam("date", "2026-05-10")
                .when().get("/api/times/availability")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
