package roomescape.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.AuthorizationException;

class JwtTokenExtractorTest {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final JwtTokenExtractor jwtTokenExtractor = new JwtTokenExtractor(
            Base64.getEncoder().encodeToString(key.getEncoded())
    );

    private String createToken(String name, String role, Date expiration) {
        return Jwts.builder()
                .setSubject("1")
                .claim("name", name)
                .claim("role", role)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
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
