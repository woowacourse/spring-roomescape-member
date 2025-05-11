package roomescape.auth.presentation;

import static org.hamcrest.Matchers.equalTo;
import static roomescape.testFixture.Fixture.MEMBER_1;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.AbstractRestDocsTest;
import roomescape.auth.presentation.dto.request.TokenRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;
import roomescape.testFixture.JdbcHelper;

class TokenLoginControllerTest extends AbstractRestDocsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @DisplayName("로그인 성공시 토큰 쿠키를 발급")
    void login_success() {
        // given
        JdbcHelper.insertMember(jdbcTemplate, MEMBER_1);

        TokenRequest request = new TokenRequest(MEMBER_1.getEmail(), MEMBER_1.getPassword());

        // when & then
        givenWithDocs("login-with-token")
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
        JdbcHelper.insertMember(jdbcTemplate, MEMBER_1);
        String payload = String.valueOf(MEMBER_1.getId());
        String token = jwtTokenProvider.createToken(payload, Role.USER);

        // when & then
        givenWithDocs("login-check")
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
