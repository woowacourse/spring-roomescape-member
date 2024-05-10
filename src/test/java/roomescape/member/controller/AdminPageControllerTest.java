package roomescape.member.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminPageControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("Admin Page 홈화면 접근 성공 테스트")
    @Test
    void responseAdminPage() {
        Response response = RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all().extract().response();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Admin Reservation Page 접근 성공 테스트")
    @Test
    void responseAdminReservationPage() {
        Response response = RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all().extract().response();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("Admin Reservation Time Page 접근 성공 테스트")
    @Test
    void responseReservationTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Admin Theme Page 접근 성공 테스트")
    @Test
    void responseThemePage() {
        RestAssured.given().log().all()
                .when().get("/admin/theme")
                .then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }
}
