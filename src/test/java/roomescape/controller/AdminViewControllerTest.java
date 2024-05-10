package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.jwt.JwtTokenProvider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminViewControllerTest {

    private static final String ADMIN_PAYLOAD = "test1@test.com";

    @LocalServerPort
    private int port;
    @Autowired
    private JwtTokenProvider provider;
    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = provider.createToken(ADMIN_PAYLOAD);
    }

    @Test
    @DisplayName("홈 화면을 요청하면 200 OK을 응답한다.")
    void adminPageTest() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 관리 페이지를 요청하면 200 OK를 반환한다.")
    void reservationPageTest() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("테마 관리 페이지를 요청하면 200 OK를 반환한다.")
    void themePageTest() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("시간 관리 페이지를 요청하면 200 OK를 반환한다.")
    void timePageTest() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
