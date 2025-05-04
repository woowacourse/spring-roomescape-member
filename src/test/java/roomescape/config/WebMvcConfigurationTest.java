package roomescape.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebMvcConfigurationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("/ 요청 시 200 OK 반환")
    @Test
    void request_welcomePage_then_200() {
        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/reservation 요청 시 200 OK 반환")
    @Test
    void request_reservationPage_then_200() {
        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin 요청 시 200 OK 응답")
    @Test
    void request_adminPage_then_200() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

}
