package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminPageControllerTest {

    @DisplayName("admin 비인가 테스트")
    @Test
    void welcomePageTest() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("Reservation Page 비인가 테스트")
    @Test
    void reservationPageTest() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("Time Page 비인가 테스트")
    @Test
    void reservationTimePageTest() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(401);
    }
}
