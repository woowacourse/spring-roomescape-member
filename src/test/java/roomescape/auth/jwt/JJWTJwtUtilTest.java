package roomescape.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.AuthToken;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.model.vo.UserRole;
import roomescape.exception.auth.NotAuthenticatedException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

class JJWTJwtUtilTest {

    private static final String SECRET_KEY = "thisisasecretkeyfortestingpurposesonly12345678901234567890";
    private static final long expirationMinute = 15L;
    private JJWTJwtUtil jwtUtil;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtUtil = new JJWTJwtUtil(SECRET_KEY, expirationMinute);
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void 유효한_사용자정보로_토큰을_생성한다() {
        // given
        User user = User.restore("1", UserRole.USER.name(), "dompoo", "dompoo@email.com", "password");

        // when
        AuthToken authToken = jwtUtil.createToken(user);

        // then
        assertThat(authToken).isNotNull();
        assertThat(authToken.value()).isNotEmpty();
    }

    @Test
    void 토큰에서_로그인정보를_추출한다() {
        // given
        String token = createToken(UserRole.USER.name());

        // when
        LoginInfo loginInfo = jwtUtil.validateAndResolveToken(token);

        // then
        assertThat(loginInfo).isNotNull();
        assertThat(loginInfo.id()).isEqualTo("1");
        assertThat(loginInfo.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void 유효한_토큰을_검증하면_예외를_던지지_않는다() {
        // given
        String token = createToken(UserRole.USER.name());

        // when & then
        assertThatCode(() -> jwtUtil.validateAndResolveToken(token))
                .doesNotThrowAnyException();
    }

    @Test
    void 유효하지_않은_토큰을_검증하면_예외를_던진다() {
        // given
        String invalidToken = "invalid.value.value";

        // when
        assertThatThrownBy(() -> jwtUtil.validateAndResolveToken(invalidToken))
                .isInstanceOf(NotAuthenticatedException.class);
    }

    @Test
    void 널_토큰을_검증하면_예외를_던진다() {
        // given
        String invalidToken = null;

        // when
        assertThatThrownBy(() -> jwtUtil.validateAndResolveToken(invalidToken))
                .isInstanceOf(NotAuthenticatedException.class);
    }

    @Test
    void 어드민_역할의_토큰을_생성하고_검증한다() {
        // given
        User user = User.restore("1", UserRole.ADMIN.name(), "dompoo", "dompoo@email.com", "password");

        // when
        AuthToken authToken = jwtUtil.createToken(user);
        LoginInfo loginInfo = jwtUtil.validateAndResolveToken(authToken.value());

        // then
        assertThat(loginInfo.userRole()).isEqualTo(UserRole.ADMIN);
    }

    private String createToken(String role) {
        return Jwts.builder()
                .subject("1")
                .signWith(key)
                .claim("role", role)
                .compact();
    }
}
