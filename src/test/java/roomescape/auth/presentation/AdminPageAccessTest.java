package roomescape.auth.presentation;

import static org.hamcrest.Matchers.containsString;
import static roomescape.testFixture.Fixture.MEMBER_1;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.DatabaseCleaner;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminPageAccessTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        RestAssured.port = port;
        databaseCleaner.clean();
    }

    @Test
    @DisplayName("ADMIN 권한의 토큰으로 관리자 페이지에 정상 접근할 수 있다")
    void admin_role_access_success() {
        // given
        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String payload = String.valueOf(memberId);
        String token = jwtTokenProvider.createToken(payload, Role.ADMIN);

        // when & then
        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString("방탈출 예약 페이지"));
    }

    @Test
    @DisplayName("토큰이 없으면 로그인 페이지로 리다이렉트")
    void no_token_redirects_to_login_with_alert() {
        RestAssured
                .given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString(AuthErrorCode.LOGIN_REQUIRED.getMessage()));
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 로그인 페이지로 리다이렉트")
    void invalid_token_redirects_to_login_with_alert() {
        RestAssured
                .given().log().all()
                .cookie("token", "invalid.jwt.token")
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString(AuthErrorCode.INVALID_TOKEN.getMessage()));
    }

    @Test
    @DisplayName("ADMIN이 아닌 경우 403.html로 리다이렉트")
    void user_role_redirects_to_403_page() {
        // given
        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = jwtTokenProvider.createToken(String.valueOf(memberId), Role.USER);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString(AuthErrorCode.FORBIDDEN_ACCESS.getMessage()));
    }
}
