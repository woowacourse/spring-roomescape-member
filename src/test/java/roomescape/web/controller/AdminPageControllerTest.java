package roomescape.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.RestAssuredTestSupport;

class AdminPageControllerTest extends RestAssuredTestSupport {

    @Test
    @DisplayName("\"/admin\"으로 GET 요청을 보낼 수 있다.")
    void adminPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/reservation\"으로 GET 요청을 보낼 수 있다.")
    void adminReservationPage() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/time\"으로 GET 요청을 보낼 수 있다.")
    void adminTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/theme\"으로 GET 요청을 보낼 수 있다.")
    void adminThemePage() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
