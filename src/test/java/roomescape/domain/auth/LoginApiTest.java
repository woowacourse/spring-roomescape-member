package roomescape.domain.auth;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.service.JwtManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("로그인 성공 시 httpOnly 쿠키가 설정된다.")
    @Test
    void loginTest1() {
        // given
        final String email = "testuser@naver.com";
        final String password = "testpassword";
        userRepository.save(User.withoutId(new Name("꾹"), email, password, Roles.USER));

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

    @DisplayName("로그인 정보를 확인하여 반환한다.")
    @Test
    void checkTest1() {
        // given

        // when

        // then
    }

}


