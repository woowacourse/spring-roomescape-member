package roomescape.admin.controller;


import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("/admin 으로 GET 요청을 보내면 어드민 페이지와 200 OK 를 받는다.")
    void getAdminPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/reservation 으로 GET 요청을 보내면 어드민 예약 관리 페이지와 200 OK 를 받는다.")
    void getAdminReservationPage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/time 으로 GET 요청을 보내면 어드민 예약 시간 관리 페이지와 200 OK 를 받는다.")
    void getAdminTimePage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/theme 으로 GET 요청을 보내면 어드민 테마 관리 페이지와 200 OK 를 받는다.")
    void getAdminThemePage() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
