package roomescape.presentation.controller.Integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserViewControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("/ 요청 시 200 OK 반환")
    @Test
    void welcomePage_userReservationPage() {
        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/reservation 요청 시 200 OK 반환")
    @Test
    void welcomePage_redirect_to_reservationPage() {
        RestAssured.given().log().all()
                .redirects().follow(false)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

}
