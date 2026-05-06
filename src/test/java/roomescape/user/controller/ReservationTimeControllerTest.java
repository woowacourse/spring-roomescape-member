package roomescape.user.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @Test
    void 예약_가능_시간_조회_테스트() {
        RestAssured.given().log().all()
                .when().get("/user/times?themeId=1&date=2026-05-05")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(11));
    }
}
