package roomescape.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class MemberPageControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("\"/\"으로 GET 요청을 보낼 수 있다.(Welcome Page)")
    void welcomePage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("\"/reservation\"으로 GET 요청을 보낼 수 있다.")
    void reservationPage() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
