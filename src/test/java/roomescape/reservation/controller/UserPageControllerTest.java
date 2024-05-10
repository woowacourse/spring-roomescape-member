package roomescape.reservation.controller;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserPageControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("유저 페이지들에 대한 요청시 200으로 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "/reservation"})
    void userPage(String url) {
        RestAssured.given().log().all()
                .when().get(url)
                .then().log().all()
                .statusCode(200);
    }
}
