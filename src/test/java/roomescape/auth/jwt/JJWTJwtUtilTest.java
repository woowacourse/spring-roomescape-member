package roomescape.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.model.vo.UserRole;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JJWTJwtUtilTest {

    private static final String SECRET_KEY = "thisisasecretkeyfortestingpurposesonly12345678901234567890";
    private JJWTJwtUtil jwtUtil;
    private User user;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtUtil = new JJWTJwtUtil(SECRET_KEY);
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        user = mock(User.class);
        when(user.email()).thenReturn("test@example.com");
        when(user.role()).thenReturn(UserRole.USER.name());
    }

    @Test
    void 유효한_사용자정보로_토큰을_생성한다() {
        // when
        Authentication authentication = jwtUtil.getAuthentication(user);

        // then
        assertThat(authentication).isNotNull();
        assertThat(authentication.token()).isNotEmpty();
        assertThat(jwtUtil.validateToken(authentication.token())).isTrue();
    }

    @Test
    void 토큰에서_로그인정보를_추출한다() {
        // given
        String token = createToken("user@example.com", UserRole.USER.name());

        // when
        LoginInfo loginInfo = jwtUtil.getAuthorization(token);

        // then
        assertThat(loginInfo).isNotNull();
        assertThat(loginInfo.email()).isEqualTo("user@example.com");
        assertThat(loginInfo.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void 유효한_토큰을_검증하면_true를_반환한다() {
        // given
        String token = createToken("user@example.com", UserRole.USER.name());

        // when
        boolean result = jwtUtil.validateToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 유효하지_않은_토큰을_검증하면_false를_반환한다() {
        // given
        String invalidToken = "invalid.token.value";

        // when
        boolean result = jwtUtil.validateToken(invalidToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 만료된_토큰을_검증하면_false를_반환한다() {
        // given
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE1MTYyMzkwMjJ9.signature";

        // when
        boolean result = jwtUtil.validateToken(expiredToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 어드민_역할의_토큰을_생성하고_검증한다() {
        // given
        when(user.role()).thenReturn(UserRole.ADMIN.name());

        // when
        Authentication authentication = jwtUtil.getAuthentication(user);
        LoginInfo loginInfo = jwtUtil.getAuthorization(authentication.token());

        // then
        assertThat(loginInfo.userRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void 예외가_발생해도_false를_반환한다() {
        // given
        String nullToken = null;

        // when, then
        assertThatCode(() -> {
            boolean result = jwtUtil.validateToken(nullToken);
            assertThat(result).isFalse();
        }).doesNotThrowAnyException();
    }

    private String createToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .signWith(key)
                .claim("role", role)
                .compact();
    }
}
