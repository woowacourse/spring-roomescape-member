package roomescape.auth.presentation.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
import roomescape.auth.infrastructure.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenLoginControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = this.port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @DisplayName("로그인 성공시 토큰 쿠키를 발급")
    void login_success() {
        // given
        String email = "email@example.com";
        String password = "password";
        String name = "멍구";
        jdbcTemplate.update("INSERT INTO members (email, password, name) VALUES (?, ?, ?)",
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

    @DisplayName("쿠키 기반 JWT로 /check 호출 시 사용자 이름을 반환한다")
    @Test
    void checkMember_withCookieToken() {
        // given
        String email = "email@example.com";
        String password = "password";
        String name = "멍구";
        Long memberId = 1L;
        jdbcTemplate.update("INSERT INTO members (id, email, password, name) VALUES (?, ?, ?, ?)",
                memberId, email, password, name);
        String token = jwtTokenProvider.createToken(String.valueOf(memberId));

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .get("login/check")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", equalTo("멍구"));
    }
}
