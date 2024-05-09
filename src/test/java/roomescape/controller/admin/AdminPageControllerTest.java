package roomescape.controller.admin;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.controller.dto.TokenRequest;

class AdminPageControllerTest extends IntegrationTestSupport {

    @LocalServerPort
    int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("어드민 권한이 있으면 어드민 페이지에 접근할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    void admin(String url) {
        String adminToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get(url)
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("유저는 어드민 페이지에 접근할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    void user(String url) {
        String userToken = RestAssured
                .given().log().all()
                .body(new TokenRequest(USER_EMAIL, USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split("=")[1];

        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get(url)
                .then().log().all()
                .statusCode(401);
    }
}
