package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminControllerTest {

    @DisplayName("/admin 페이지 응답 -> 200")
    @Test
    void getAdminPage() {
        RestAssured.given().log().all()
            .when().get("/admin")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("/admin/reservation 페이지 응답 -> 200")
    @Test
    void getReservationPage() {
        RestAssured.given().log().all()
            .when().get("/admin/reservation")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("/admin/time 페이지 응답 -> 200")
    @Test
    void getReservationTimePage() {
        RestAssured.given().log().all()
            .when().get("/admin/time")
            .then().log().all()
            .statusCode(200);
    }

    @DisplayName("/admin/theme 페이지 응답 -> 200")
    @Test
    void getThemePage() {
        RestAssured.given().log().all()
            .when().get("/admin/theme")
            .then().log().all()
            .statusCode(200);
    }
}
