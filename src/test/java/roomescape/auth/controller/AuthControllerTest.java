package roomescape.auth.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/recreate_table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("로그인 컨트롤러")
class AuthControllerTest {

    @LocalServerPort
    private int port;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthControllerTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인 컨트롤러는 로그인 요청이 들어오면 쿠키에 액세스 토큰을 반환한다.")
    @Test
    void login() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("email", "test@gmail.com");
        body.put("password", "password");

        // when
        String accessToken = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header(HttpHeaders.SET_COOKIE)
                .split(";")[0]
                .split("token=")[1];

        String actual = jwtTokenProvider.decode(accessToken, "email");

        // then
        assertThat(actual).isEqualTo("test@gmail.com");
    }

    @DisplayName("로그인 컨트롤러는 로그인 확인 요청이 토큰에 맞는 사용자 이름을 반환한다.")
    @Test
    void checkLogin() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("email", "test@gmail.com");
        body.put("password", "password");

        String accessToken = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header(HttpHeaders.SET_COOKIE)
                .split(";")[0]
                .split("token=")[1];

        // when
        String actual = RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath().get("name");

        // then
        assertThat(actual).isEqualTo("클로버");
    }
}
