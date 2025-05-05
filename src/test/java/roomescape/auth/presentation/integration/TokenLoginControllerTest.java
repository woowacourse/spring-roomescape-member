package roomescape.auth.presentation.integration;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.auth.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenLoginControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = port;
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }

    @Test
    @DisplayName("로그인 성공시 토큰 쿠키를 발급")
    void login_success() {
        // given
        String email = "email@example.com";
        String password = "password";
        String name = "멍구";
        jdbcTemplate.update("INSERT INTO users (email, password, name) VALUES (?, ?, ?)",
                email, password, name);
        TokenRequest request = new TokenRequest(email, password);

        // when & then
        given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .header("Set-Cookie", Matchers.containsString("token="));
    }
}
