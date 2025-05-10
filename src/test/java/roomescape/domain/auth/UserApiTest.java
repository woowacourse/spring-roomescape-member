package roomescape.domain.auth;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("회원 정보를 등록한다")
    @Test
    void registerTest1() {
        // given
        final String name = "테스트유저";
        final String email = "testuser@naver.com";
        final String password = "testpassword";
        final Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("email", email);
        request.put("password", password);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @DisplayName("이미 존재하는 이메일로 등록할 시 409 예외를 반환한다.")
    @Test
    void registerTest2() {
        final String name = "테스트유저";
        final String email = "testuser@naver.com";
        final String password = "testpassword";

        userRepository.save(User.withoutId(new Name(name), email, password, Roles.USER));

        final Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("email", email);
        request.put("password", password);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(409);
    }

    @DisplayName("이메일이 존재하지 않는다면 401 예외를 반환한다.")
    @Test
    void loginTest3() {
        // given
        final Map<String, String> request = new HashMap<>();
        request.put("email", "notfound@naver.com");
        request.put("password", "any");

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

    @DisplayName("중복된 이메일로 회원가입 시 409 예외를 반환한다.")
    @Test
    void registerDuplicateEmailTest() {
        // given
        final String email = "duplicate@naver.com";
        final String password = "password";
        final String name = "테스트유저";

        userRepository.save(User.withoutId(new Name(name), email, password, Roles.USER));

        final Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("email", email);
        request.put("password", password);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(409);
    }



}
