package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.auth.exception.ExpiredTokenException;
import roomescape.auth.exception.InvalidTokenException;

class TokenManagerTest {
    private static final String TEST_TOKEN_SECRET = "test-secret".repeat(10);
    private static final long millis = 1000L * 60;
    private final Clock clock = Clock.fixed(Instant.parse("2000-01-01T00:00:00Z"), ZoneId.systemDefault());

    private final TokenManager tokenManager = new TokenManager(TEST_TOKEN_SECRET, millis, clock);

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 비어있는 경우, 예외를 발생한다.")
    void invalidTokenOnBlankOrNull(String token) {
        assertThatCode(() -> tokenManager.getMemberIdFrom(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰에서 유저 아이디를 가져온다.")
    void extractFromCookiesTest() {
        String token = Jwts.builder()
                .setSubject("1")
                .signWith(Keys.hmacShaKeyFor(TEST_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
        long memberId = tokenManager.getMemberIdFrom(token);
        assertThat(memberId).isEqualTo(1);
    }

    @Test
    @DisplayName("만료된 토큰을 가져오는 경우, 예외를 발생한다.")
    void expiredTokenTest() {
        Date expireDate = Date.from(clock.instant().minusMillis(millis + 1));
        String token = Jwts.builder()
                .setSubject("1")
                .signWith(Keys.hmacShaKeyFor(TEST_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .setExpiration(expireDate)
                .compact();
        assertThatCode(() -> tokenManager.getMemberIdFrom(token))
                .isInstanceOf(ExpiredTokenException.class);
    }
}
