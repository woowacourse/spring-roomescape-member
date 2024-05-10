package roomescape.web.controller;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class AuthControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("로그인 성공 시, 토큰을 발급한다.")
    void loginWithValidEmailAndPassword() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "hong@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 시, 일치하는 이메일이 없으면 예외가 발생한다.")
    void loginWithInvalidEmail() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "jo@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("로그인 시, 비밀번호가 일치하지 않으면 예외가 발생한다.")
    void loginWithInvalidPassword() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "hong@gmail.com");
        params.put("password", "55555");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }
}
