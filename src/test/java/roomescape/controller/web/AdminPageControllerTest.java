package roomescape.controller.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class AdminPageControllerTest {
    @LocalServerPort
    private int port;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "planet@gmail.com");
        loginParams.put("password", "1111");

        adminToken = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];
    }

    @Test
    @DisplayName("관리자 메인 페이지로 이동한다.")
    void moveToAdminPage() {
        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 관리 페이지로 이동한다.")
    void moveToAdminReservationPage() {
        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("시간 관리 페이지로 이동한다.")
    void moveToAdminTimePage() {
        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 관리 페이지로 이동한다.")
    void moveToAdminThemePage() {
        RestAssured.given().log().all()
                .header("Cookie", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 아니면, 예약 관리 페이지로 이동할 수 없다.")
    void cannotMoveToAdminReservationPage() {
        final Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "imjojo@gmail.com");
        loginParams.put("password", "qwer");

        final String memberToken = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        RestAssured.given().log().all()
                .header("Cookie", memberToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(401);
    }
}
