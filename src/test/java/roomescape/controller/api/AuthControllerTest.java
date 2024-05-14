package roomescape.controller.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import roomescape.dto.response.LoginCheckResponseDto;

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
        params.put("email", "imjojo@gmail.com");
        params.put("password", "qwer");

        final String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie");

        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("로그인 시, 일치하는 이메일이 없으면 예외가 발생한다.")
    void loginWithInvalidEmail() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "nobody@gmail.com");
        params.put("password", "1234");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("로그인 시, 비밀번호가 일치하지 않으면 예외가 발생한다.")
    void loginWithInvalidPassword() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "imjojo@gmail.com");
        params.put("password", "55555");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("로그인 후 인증 정보를 조회한다.")
    void checkLogin() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "imjojo@gmail.com");
        params.put("password", "qwer");

        final String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        final LoginCheckResponseDto response = RestAssured
                .given().log().all()
                .header("Cookie", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().as(LoginCheckResponseDto.class);

        assertThat(response).isNotNull()
                .extracting(LoginCheckResponseDto::getName)
                .isEqualTo("조조");
    }

    @Test
    @DisplayName("인증 정보 조회 시, 토큰이 유효하지 않으면 예외가 발생한다.")
    void checkLoginWithInvalidToken() {
        final String invalidCookie = "token=invalid-token";

        RestAssured.given().log().all()
                .header("Cookie", invalidCookie)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("로그아웃 시, 쿠키를 삭제한다.")
    void logout() {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", "imjojo@gmail.com");
        params.put("password", "qwer");

        final String token = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        final String logoutCookie = RestAssured.given().log().all()
                .header("Cookie", token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        assertThat(logoutCookie).isEqualTo("token=");
    }
}
