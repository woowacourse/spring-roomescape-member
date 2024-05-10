package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate-data.sql")
class LoginControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("로그인 시 토큰을 응답 받는다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void login_Success() {
        Map<String, String> params = Map.of(
                "email", "naknak@example.com",
                "password", "nak123"
        );

        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        assertThat(header).matches("^token=[^.]+\\.[^.]+\\.[^.]+; Path=\\/; HttpOnly$");
    }

    @Test
    @DisplayName("로그인 시 받은 토큰으로 인증을 조회한다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void auth_Success() {
        Map<String, String> params = Map.of(
                "email", "naknak@example.com",
                "password", "nak123"
        );

        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        String[] parts = header.split(";");
        String token = parts[0].split("=")[1];

        Map<String, String> cookies = Map.of(
                "_ga", "GA1.1.48222725.1666268105",
                "_ga_QD3BVX7MKT", "GS1.1.1687746261.15.1.1687747186.0.0.0",
                "Idea-25a74f9c", "3cbc3411-daca-48c1-8201-51bdcdd93164",
                "token", token
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("naknak"));
    }
}
