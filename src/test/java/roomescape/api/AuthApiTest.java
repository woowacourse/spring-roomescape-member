package roomescape.api;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthApiTest {

    private static final Map<String, Object> MEMBER_BODY = new HashMap<>();
    private static final Map<String, Object> AUTH_BODY = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;
    private final int port;

    public AuthApiTest(
            @LocalServerPort final int port,
            @Autowired final JdbcTemplate jdbcTemplate
    ) {
        this.port = port;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeAll
    static void beforeAll() {
        AUTH_BODY.put("email", "admin@email.com");
        AUTH_BODY.put("password", "password");

        MEMBER_BODY.put("email", "admin@email.com");
        MEMBER_BODY.put("password", "password");
        MEMBER_BODY.put("name", "어드민");
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM member");
        jdbcTemplate.update("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("로그인에 성공하면 200을 응답하고, Set-Cookie 헤더에 토큰이 담긴다.")
    @Test
    void post() {
        // given
        givenCreateMember(AUTH_BODY.get("password").toString());

        // when & then
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(AUTH_BODY)
                .when().post("/login")
                .then().log().all().statusCode(200)
                .header(HttpHeaders.SET_COOKIE.toString(), notNullValue());
    }

    @DisplayName("존재하지 않는 이메일이라면, 400을 응답한다.")
    @Test
    void post1() {
        // given & when & then
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(AUTH_BODY)
                .when().post("/login")
                .then().log().all().statusCode(400)
                .header(HttpHeaders.SET_COOKIE.toString(), nullValue());
    }

    @DisplayName("비밀번호가 일치하지 않는다면, 400을 응답한다.")
    @Test
    void post2() {
        givenCreateMember("another password");

        // given & when & then
        RestAssured.given().port(port).log().all()
                .contentType(ContentType.JSON)
                .body(AUTH_BODY)
                .when().post("/login")
                .then().log().all().statusCode(400)
                .header(HttpHeaders.SET_COOKIE.toString(), nullValue());
    }

    private void givenCreateMember(final String password) {
        final Map<String, Object> body = new HashMap<>(MEMBER_BODY);
        body.put("password", password);

        RestAssured.given().port(port)
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/members")
                .then().statusCode(201);
    }

}
