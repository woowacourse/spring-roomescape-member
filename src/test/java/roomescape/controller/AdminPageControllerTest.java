package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.dto.LoginRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminPageControllerTest {

    String accessToken;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequestDto("admin@email.com", "admin"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .extract().header("Set-Cookie").split("=")[1];
    }

    @DisplayName("어드민 페이지를 호출 시 200으로 응답한다.")
    @Test
    void adminPageTest() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 페이지 호출 시 200으로 응답한다.")
    @Test
    void reservationPageTest() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 테마 페이지 요청 시 200으로 응답한다.")
    @Test
    void themePageTest() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 시간 페이지 요청 시 200으로 응답한다.")
    @Test
    void timeTest() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
