package roomescape.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.RestAssuredTestSupport;

class AdminPageControllerTest extends RestAssuredTestSupport {

    @Test
    @DisplayName("\"/admin\"으로 GET 요청을 보낼 수 있다.")
    void openAdminPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/reservation\"으로 GET 요청을 보낼 수 있다.")
    void openAdminReservationPage() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/admin/time\"으로 GET 요청을 보낼 수 있다.")
    void openAdminTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
