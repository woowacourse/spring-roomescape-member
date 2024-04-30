package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoadControllerTest {
    @LocalServerPort
    private int port;

    @DisplayName("관리자 페이지를 열 수 있다.")
    @Test
    void loadAdminPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 페이지를 열 수 있다.")
    @Test
    void loadReservationPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간 관리 페이지를 열 수 있다.")
    @Test
    void loadTimePage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }
}
