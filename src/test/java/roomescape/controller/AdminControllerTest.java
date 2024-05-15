package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest {
    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        token = RestAssured.given().log().all()
                .body(new TokenRequest("password", "admin@email.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }

    @DisplayName("Admin Page 홈화면 접근 성공 테스트")
    @Test
    void responseAdminPage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Admin Reservation Page 접근 성공 테스트")
    @Test
    void responseAdminReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Admin Reservation Time Page 접근 성공 테스트")
    @Test
    void responseReservationTimePage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Admin Theme Page 접근 성공 테스트")
    @Test
    void responseThemePage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("Admin reservations Page 접근 성공 테스트")
    @Test
    void responseAdminReservationsPage() {
        AdminReservationRequest adminReservationRequest =
                new AdminReservationRequest("2222-05-15", 1, 1, 1);

        RestAssured.given().log().all()
                .body(adminReservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie("token", token)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }
}
