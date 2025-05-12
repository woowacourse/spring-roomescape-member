package roomescape.controller.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.auth.LoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/schema.sql", "/fixtures/member.sql"})
class AuthenticationControllerTest {

    @LocalServerPort
    private int port;

    private RequestSpecification spec;

    @BeforeEach
    void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(port)
                .build();

        RestAssured.requestSpecification = spec;
    }

    @Test
    @DisplayName("로그인 성공 시, 쿠키를 반환해야한다")
    void login_ShouldSetTokenCookie() {
        // given
        String email = "user@example.com";
        String password = "user123";
        LoginRequest request = new LoginRequest(email, password);

        // when & then
        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .header("Keep-Alive", "timeout=60")
                .extract();

        // 쿠키의 속성들을 검증합니다
        String setCookieHeader = response.header("Set-Cookie");
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("Path=/");
        assertThat(setCookieHeader).contains("HttpOnly");
    }

    @Test
    @DisplayName("로그인 체크 시 유효한 토큰이 있다면, name 값을 반환해야한다")
    void loginCheck() {
        // given
        String name = "user";
        String email = "user@example.com";
        String password = "user123";
        LoginRequest request = new LoginRequest(email, password);

        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .header("Keep-Alive", "timeout=60")
                .extract();
        String givenToken = response.cookie("token");

        // when & then
        RestAssured.given()
                .cookie("token", givenToken)
                .when()
                .get("/auth/login/check")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid-token"})
    @DisplayName("로그인 체크 시, 토큰이 유효하지 않다면 401 코드를 반환해야한다")
    void loginCheck_ShouldReturnUnauthorizedWhenInvalidToken(String token) {
        // when & then
        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/auth/login/check")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .contentType(ContentType.JSON)
                .body("name", org.hamcrest.Matchers.nullValue());
    }
}
