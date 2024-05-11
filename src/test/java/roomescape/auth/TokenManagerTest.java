package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import roomescape.application.ServiceTest;
import roomescape.auth.exception.ExpiredTokenException;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.domain.role.MemberRole;

@ServiceTest
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private Clock clock;

    @Value("${jwt.secret}")
    private String TEST_TOKEN_SECRET;

    @Value("${jwt.expire-in-millis}")
    private long millis;

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 비어있는 경우, 예외를 발생한다.")
    void invalidTokenOnBlankOrNull(String token) {
        assertThatCode(() -> tokenManager.extract(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰에서 유저 아이디를 가져온다.")
    void extractFromCookiesTest() {
        String token = Jwts.builder()
                .setSubject("1")
                .claim("name", "test")
                .claim("role", "member")
                .signWith(Keys.hmacShaKeyFor(TEST_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
        MemberRole memberToken = tokenManager.extract(token);
        assertThat(memberToken.getMemberId()).isEqualTo(1);
    }

    @Test
    @DisplayName("만료된 토큰을 가져오는 경우, 예외를 발생한다.")
    void expiredTokenTest() {
        Date expireDate = Date.from(clock.instant().minusMillis(millis + 1));
        String token = Jwts.builder()
                .setSubject("1")
                .claim("name", "test")
                .claim("role", "member")
                .signWith(Keys.hmacShaKeyFor(TEST_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .setExpiration(expireDate)
                .compact();
        assertThatCode(() -> tokenManager.extract(token))
                .isInstanceOf(ExpiredTokenException.class);
    }
}
