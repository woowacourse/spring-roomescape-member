package roomescape.config.handler;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminAuthenticationInterceptorTest {
    private static final String TEST_ADMIN_ENDPOINT = "/admin/handler-test";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인하지 않으면, 어드민 페이지에 접근할 수 없다.")
    @Test
    void preHandleTest_whenNotLogin() {
        RestAssured.given().log().all()
                .when().get(TEST_ADMIN_ENDPOINT)
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("유저가 로그인하면, 어드민 페이지에 접근할 수 없다.")
    @Test
    void preHandleTest_whenNotUserLogin() {
        Cookies userCookies = makeUserCookie();

        RestAssured.given().log().all()
                .cookies(userCookies)
                .when().get(TEST_ADMIN_ENDPOINT)
                .then().log().all()
                .statusCode(403);
    }

    private Cookies makeUserCookie() {
        LoginRequest request = new LoginRequest("bri@abc.com", "1234");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().detailedCookies();
    }

    @DisplayName("관리자가 로그인하면, 어드민 페이지에 접근할 수 있다.")
    @Test
    void preHandleTest_whenNotAdminLogin() {
        Cookies adminCookies = makeAdminCookie();

        RestAssured.given().log().all()
                .cookies(adminCookies)
                .when().get(TEST_ADMIN_ENDPOINT)
                .then().log().all()
                .statusCode(200);
    }

    private Cookies makeAdminCookie() {
        LoginRequest request = new LoginRequest("admin@abc.com", "1234");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().detailedCookies();
    }

}
