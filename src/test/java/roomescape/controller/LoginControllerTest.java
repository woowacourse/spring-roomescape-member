package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("Login Page 접근 성공 테스트")
    @Test
    void responseLoginPage() {
        Response response = RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all().extract().response();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
