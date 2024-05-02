package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("어드민 컨트롤러")
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("어드민 컨트롤러는 /admin으로 GET 요청이 들어오면 어드민 페이지를 반환한다.")
    @Test
    void readAdminPage() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/reservation으로 GET 요청이 들어오면 예약 목록을 페이지를 반환한다.")
    @Test
    void readReservations() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/time으로 GET 요청이 들어오면 시간 목록을 페이지를 반환한다.")
    @Test
    void readTimes() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 컨트롤러는 /admin/theme로 GET 요청이 들어오면 테마 목록 페이지를 반환한다.")
    @Test
    void readTheme() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
