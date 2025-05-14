package roomescape.integration.user;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.MemberRole;
import roomescape.utility.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;


    @DisplayName("로그인할 수 있다")
    @Test
    void canLogin() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "ecxewqe!23");

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("access", notNullValue());
    }

    @DisplayName("로그인할 때, 이메일에 대한 계정이 존재하지 않을 경우 로그인할 수 없다")
    @Test
    void cannotLoginBecauseOfInvalidEmail() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "ecxewqe!23");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그인할 때, 비밀번호가 맞지 않을 경우 로그인할 수 없다")
    @Test
    void cannotLoginBecauseOfWrongPassword() {
        // given
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "wrong_password_123!");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그인 여부를 확인할 수 있다")
    @Test
    void canCheckLogin() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("회원"));
    }

    @DisplayName("로그인 체크를 할때, 토큰이 올바르지 않은 경우 예외가 발생한다")
    @Test
    void cannotCheckLoginBecauseOfInvalidToken() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);
        String invalidToken = accessToken + "invalid";

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", invalidToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그인 체크를 할때, 토큰이 만료된 경우 예외가 발생한다")
    @Test
    void cannotCheckLoginBecauseOfTokenExpiration() {
        // given
        Date validity = new Date(new Date().getTime() - 1000);
        String expirationToken = Jwts.builder()
                .setSubject(String.valueOf(1L))
                .claim("name", "회원")
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, tokenSecretKey)
                .compact();

        // when & then
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", expirationToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그아웃 할 수 있다")
    @Test
    void canLogout() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원", MemberRole.GENERAL);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200);
    }
}
