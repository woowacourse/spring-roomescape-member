package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import roomescape.service.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogoutControllerTest {
    @LocalServerPort
    int port;

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

    @DisplayName("Logout 성공 테스트")
    @Test
    void responseLoginPage() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200);
    }
}
