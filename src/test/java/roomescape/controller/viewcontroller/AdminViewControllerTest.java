package roomescape.controller.viewcontroller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminViewControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("Admin Page 홈화면 접근 성공 테스트")
    @Test
    void responseAdminPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Reservation Page 접근 성공 테스트")
    @Test
    void responseAdminReservationPage() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Time Page 접근 성공 테스트")
    @Test
    void responseAdminTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Theme Page 접근 성공 테스트")
    @Test
    void responseAdminThemePage() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }
}
