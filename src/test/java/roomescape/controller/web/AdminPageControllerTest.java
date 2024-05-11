package roomescape.controller.web;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class AdminPageControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자 메인 페이지로 이동한다.")
    void moveToAdminPage() {
        final String adminToken = getAdminToken();

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 관리 페이지로 이동한다.")
    void moveToAdminReservationPage() {
        final String adminToken = getAdminToken();

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("시간 관리 페이지로 이동한다.")
    void moveToAdminTimePage() {
        final String adminToken = getAdminToken();

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 관리 페이지로 이동한다.")
    void moveToAdminThemePage() {
        final String adminToken = getAdminToken();

        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 아니면, 예약 관리 페이지로 이동할 수 없다.")
    void cannotMoveToAdminReservationPage() {
        final String memberToken = getMemberToken();

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(401);
    }

    private String getMemberToken() {
        return getLoginToken("imjojo@gmail.com", "qwer");
    }

    private String getAdminToken() {
        return getLoginToken("admin@gmail.com", "12345");
    }

    private String getLoginToken(final String email, final String password) {
        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", email);
        loginParams.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }
}
