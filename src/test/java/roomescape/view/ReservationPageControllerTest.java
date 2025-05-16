package roomescape.view;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationPageControllerTest {

    @DisplayName("예약 페이지를 불러 온다")
    @Test
    void getReservationPageTest() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("인기 테마 페이지를 불러 온다")
    @Test
    void getPopularThemePageTest() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }
}
