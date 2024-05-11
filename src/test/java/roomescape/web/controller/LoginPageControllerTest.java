package roomescape.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class LoginPageControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("login 페이지 조회가 가능하다.")
    void loginPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }
}
