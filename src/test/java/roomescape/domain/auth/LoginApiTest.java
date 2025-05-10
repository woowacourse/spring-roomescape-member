package roomescape.domain.auth;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("로그인 성공 시 httpOnly 쿠키가 설정된다.")
    @Test
    void loginTest1() {
        // given
        final String email = "testuser@naver.com";
        final String rawPassword = "testpassword";
        final Password password = Password.encrypt(rawPassword, passwordEncryptor);
        userRepository.save(User.withoutId(new Name("꾹"), email, password, Roles.USER));

        final LoginRequest loginRequest = new LoginRequest(email, rawPassword);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .header(HttpHeaders.SET_COOKIE, containsString("token="))
                .header(HttpHeaders.SET_COOKIE, containsString("HttpOnly"));
    }

    @DisplayName("로그인 실패 시 401 예외를 반환한다.")
    @Test
    void loginTest2() {
        // given
        final String email = "wronguser@naver.com";
        final String password = "wrongpassword";
        final LoginRequest loginRequest = new LoginRequest(email, password);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .log()
                .all()
                .statusCode(401);
    }

    @DisplayName("비밀번호가 다르다면 401 예외를 반환한다.")
    @Test
    void loginTest3() {
        // given
        final String name = "테스트유저";
        final String email = "testuser3@naver.com";
        final Password password = Password.encrypt("testpassword", passwordEncryptor);
        userRepository.save(User.withoutId(new Name(name), email, password, Roles.USER));

        final Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("password", "wrongpassword");

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .log()
                .all()
                .statusCode(401);
    }

    @DisplayName("로그인 시 이메일 형식이 잘못되면 400 예외를 반환한다.")
    @Test
    void loginInvalidEmailFormatTest() {
        // given
        final Map<String, String> request = new HashMap<>();
        request.put("email", "invalid-email");
        request.put("password", "password");

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/login")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @DisplayName("쿠키를 통해 인증 정보를 조회할 수 있다.")
    @Test
    void checkTest1() {
        // given
        final String name = "테스트유저";
        final String email = "testuser4@naver.com";
        final String rawPassword = "testpassword";
        final Password password = Password.encrypt(rawPassword, passwordEncryptor);

        userRepository.save(User.withoutId(new Name(name), email, password, Roles.USER));

        final Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", rawPassword);

        // 로그인하여 토큰 쿠키 획득
        final String token = RestAssured.given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract()
                .cookie("token");

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/login/check")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @DisplayName("잘못된 쿠키는 401 예외를 반환한다.")
    @Test
    void checkTest2() {
        // when & then
        RestAssured.given()
                .cookie("token", "invalidtoken")
                .when()
                .get("/login/check")
                .then()
                .log()
                .all()
                .statusCode(401);
    }

    @DisplayName("회원 정보를 조회할 때 인증되지 않은 사용자는 401 예외를 반환한다.")
    @Test
    void getUserInfoUnauthorizedTest() {
        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/login/check")
                .then()
                .log()
                .all()
                .statusCode(401);
    }
}


