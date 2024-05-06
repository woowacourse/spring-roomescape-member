package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("어드민 메인 페이지 호출 테스트")
    @Test
    void admin_main_page() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 예약 관리 페이지 호출 테스트")
    @Test
    void admin_reservation_page() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 시간 관리 페이지 호출 테스트")
    @Test
    void admin_time_page() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 테마 관리 페이지 호출 테스트")
    @Test
    void admin_theme_page() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
