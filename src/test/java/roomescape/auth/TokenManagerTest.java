package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TokenManagerTest {
    private static final String TEST_TOKEN_SECRET = "test-secret".repeat(10);

    private final TokenManager tokenManager = new TokenManager(TEST_TOKEN_SECRET);

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 비어있는 경우, 예외를 발생한다.")
    void invalidTokenOnBlankOrNull(String token) {
        assertThatCode(() -> tokenManager.getMemberIdFromToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토큰 값이 없습니다.");
    }

    @Test
    @DisplayName("쿠키에서 유저 아이디를 가져온다.")
    void extractFromCookiesTest() {
        String token = Jwts.builder()
                .claim("memberId", 1)
                .signWith(Keys.hmacShaKeyFor(TEST_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
        new Cookie("token", token);
        Cookie[] cookies = {new Cookie("token", token)};
        long memberId = tokenManager.getMemberIdFromCookies(cookies);
        assertThat(memberId).isEqualTo(1);
    }

    @Test
    @DisplayName("쿠키에 토큰 값이 없는 경우, 예외를 발생한다.")
    void cookieWithoutTokenTest() {
        Cookie[] cookies = {};
        assertThatCode(() -> tokenManager.getMemberIdFromCookies(cookies))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토큰 값이 없습니다.");
    }
}
