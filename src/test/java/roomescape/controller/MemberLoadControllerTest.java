package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberLoadControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("인기 테마 페이지를 열 수 있다.")
    @Test
    void loadPopularThemePage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 예약 페이지를 열 수 있다.")
    @Test
    void loadUserReservationPage() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 로그인 페이지를 열 수 있다.")
    @Test
    void loadLoginPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 회원가입 페이지를 열 수 있다.")
    @Test
    void loadSignUpPage() {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .statusCode(200);
    }
}
