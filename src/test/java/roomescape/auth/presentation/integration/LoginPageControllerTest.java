package roomescape.auth.presentation.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginPageControllerTest {

    @LocalServerPort
    int port;

    @DisplayName("/login 페이지 접속 시 200 OK")
    @Test
    void loginPage_viewReturn() {
        RestAssured.port = this.port;

        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

}
