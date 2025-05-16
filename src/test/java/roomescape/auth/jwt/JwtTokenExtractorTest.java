package roomescape.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.AuthorizationException;

class JwtTokenExtractorTest {

    private final String secretKey = "test-secret-key-for-jwt-which-should-be-long-enough";
    private final Algorithm algorithm = Algorithm.HMAC256(secretKey);
    private final JwtTokenExtractor jwtTokenExtractor = new JwtTokenExtractor(secretKey);

    private String createToken(String name, String role, Date expiration) {
        return JWT.create()
                .withSubject("1")
                .withClaim("name", name)
                .withClaim("role", role)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    @DisplayName("토큰에서 멤버 아이디를 추출한다")
    @Test
    void extractMemberIdFromValidTokenTest() {
        String token = createToken("사용자", "USER", new Date(System.currentTimeMillis() + 60000));
        assertThat(jwtTokenExtractor.extractMemberIdFromToken(token)).isEqualTo("1");
    }

    @DisplayName("토큰에서 멤버 역할을 추출한다")
    @Test
    void extractMemberRoleFromValidTokenTest() {
        String token = createToken("어드민", "ADMIN", new Date(System.currentTimeMillis() + 60000));
        assertThat(jwtTokenExtractor.extractMemberRoleFromToken(token)).isEqualTo("ADMIN");
    }

    @DisplayName("토큰이 null이거나 비어있는 경우 예외를 던진다")
    @Test
    void extractMemberIdFromTokenTest_WhenTokenIsNullOrBlank() {
        assertThatThrownBy(() -> jwtTokenExtractor.extractMemberIdFromToken(null))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("로그인 토큰이 존재하지 않습니다");

        assertThatThrownBy(() -> jwtTokenExtractor.extractMemberIdFromToken(" "))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("로그인 토큰이 존재하지 않습니다");
    }

    @DisplayName("토큰이 만료된 경우 예외를 던진다")
    @Test
    void extractMemberIdFromTokenTest_WhenTokenIsExpired() {
        String expiredToken = createToken("사용자", "USER", new Date(System.currentTimeMillis() - 1000));
        assertThatThrownBy(() -> jwtTokenExtractor.extractMemberIdFromToken(expiredToken))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("토큰이 만료 되었습니다");
    }

    @DisplayName("서명이 올바르지 않거나 잘못된 경우 예외를 던진다")
    @Test
    void extractMemberIdFromTokenTest_WhenTokenIsTampered() {
        String validToken = createToken("사용자", "USER", new Date(System.currentTimeMillis() + 60000));
        String tamperedToken = validToken + "tamperedSignature";

        assertThatThrownBy(() -> jwtTokenExtractor.extractMemberIdFromToken(tamperedToken))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("서명이 올바르지 않거나 잘못된 토큰입니다");
    }
}
