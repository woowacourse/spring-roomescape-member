package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("DB에 저장된 이메일과 비밀번호로 로그인 시도하면 토큰으로 쿠키에 저장할 수 있다.")
    @Test
    void login() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");

        Map<String, String> params = Map.of(
                "email", "aaa@naver.com",
                "password", "1111"
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .log().all().extract();

        String keepAlive = response.header("Keep-Alive");
        String accessToken = response.header("Set-Cookie").split(";")[0];

        assertAll(
                () -> assertThat(keepAlive).isEqualTo("timeout=60"),
                () -> assertThat(accessToken.startsWith("token=")).isTrue()
        );
    }

    @DisplayName("DB에 저장되지 않은 이메일과 비밀번호로 로그인 시도하면 로그인이 안된다.")
    @Test
    void login_whenNotMember() {
        Map<String, String> params = Map.of(
                "email", "abc@email.com",
                "password", "1111"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(203);
    }

    @DisplayName("로그인 이후에 로그인 정보와 관련된 이름을 받을 수 있다.")
    @Test
    void checkLogin() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");

        Map<String, String> params = Map.of(
                "email", "aaa@naver.com",
                "password", "1111"
        );

        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract().header("Set-Cookie").split(";")[0];

        String name = RestAssured.given().log().all()
                .header("cookie", cookie)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getJsonObject("name");


        assertThat(name).isEqualTo("켬미");
    }
}
