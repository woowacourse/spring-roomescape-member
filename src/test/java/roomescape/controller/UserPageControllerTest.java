package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserPageControllerTest {

    @Test
    @DisplayName("User 메인 페이지를 렌더링한다")
    void displayMainPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("User 예약 목록 페이지를 렌더링한다")
    void displayReservationPage() {
        RestAssured.given().log().all()
                .when().get("reservation")
                .then().log().all()
                .statusCode(200);
    }
}
