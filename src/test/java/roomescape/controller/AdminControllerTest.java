package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.Fixtures;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("어드민 컨트롤러")
class AdminControllerTest {

    @LocalServerPort
    private int port;
    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = Fixtures.login("pedro@me.com", "11111");
    }

    @DisplayName("어드민 컨트롤러는 /admin으로 GET 요청이 들어오면 어드민 페이지를 반환한다.")
    @Test
    void readAdminPage() {
        RestAssured.given().log().all()
                .cookie("auth_token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/reservation으로 GET 요청이 들어오면 예약 목록 페이지를 반환한다.")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .cookie("auth_token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/time으로 GET 요청이 들어오면 시간 목록 페이지를 반환한다.")
    @Test
    void readTimes() {
        RestAssured.given().log().all()
                .cookie("auth_token", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/theme로 GET 요청이 들어오면 테마 목록 페이지를 반환한다.")
    @Test
    void readTheme() {
        RestAssured.given().log().all()
                .cookie("auth_token", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 어드민 계정이 아닌 계정으로 접속할 경우 403을 응답한다.")
    @Test
    void accessWithNormalAccount() {
        String email = "clover@me.com";
        String password = "22222";

        RestAssured.given().log().all()
                .cookie("auth_token", Fixtures.login(email, password))
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }
}
