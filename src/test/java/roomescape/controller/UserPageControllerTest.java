package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserPageControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("/reservation 요청시 사용자 예약 페이지를 응답한다.")
    @Test
    void response_user_reservation_page() {
        RestAssured.given()
                .when().get("/reservation")
                .then()
                .statusCode(200);
    }

    @DisplayName("/ 요청시 인기 테마 페이지를 응답한다.")
    @Test
    void response_popular_theme_page() {
        RestAssured.given()
                .when().get("/")
                .then()
                .statusCode(200);
    }

    @DisplayName("/login 요청 시 로그인 폼이 있는 페이지를 응답한다.")
    @Test
    void response_login_page() {
        RestAssured.given()
                .when().get("/login")
                .then()
                .statusCode(200);
    }
}
