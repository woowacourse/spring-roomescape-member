package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ViewIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("루트 화면을 요청하면 200 OK를 응답한다.")
    void popularThemePageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 페이지를 요청하면 200 OK를 응답한다.")
    void reservationPageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지를 요청하면 200 OK를 응답한다.")
    void loginPageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("회원가입 페이지를 요청하면 200 OK를 응답한다.")
    void signupPageTest() throws Exception {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .statusCode(200);
    }
}
