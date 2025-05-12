package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.application.AuthorizationException;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;

class AuthenticatorTest {
    private Authenticator authenticator;

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final long validityInMilliseconds = 3600000;
    private final SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    private final String memberId = "1";
    private final String role = "member";

    @BeforeEach
    void setUp() {
        authenticator = new Authenticator(secretKey, validityInMilliseconds);
    }

    @Test
    @DisplayName("페이로드로 토큰을 생성한다")
    void test1() {
        // given
        Payload payload = Payload.from(memberId, role);

        // when
        Token token = authenticator.authenticate(payload);

        // then
        assertThat(token).isNotNull();
        assertThat(token.value()).isNotBlank();
    }

    @Test
    @DisplayName("유효한 토큰을 검증하면 false를 반환한다")
    void test2() {
        // given
        Payload payload = Payload.from(memberId, role);
        Token token = authenticator.authenticate(payload);

        // when & then
        assertFalse(authenticator.isInvalidAuthentication(token));
    }

    @Test
    @DisplayName("만료된 토큰을 검증하면 true를 반환한다")
    void test3() {
        // given
        Date now = new Date();
        Date pastDate = new Date(now.getTime() - 1000);

        String expiredToken = Jwts.builder()
                .setSubject(memberId)
                .claim(Authenticator.ROLE_CLAIM_EXPRESSION, role)
                .setIssuedAt(pastDate)
                .setExpiration(pastDate)
                .signWith(signingKey)
                .compact();

        // when & then
        assertTrue(authenticator.isInvalidAuthentication(new Token(expiredToken)));
    }

    @Test
    @DisplayName("잘못된 형식의 토큰을 검증하면 true를 반환한다")
    void test4() {
        // given
        Token invalidToken = new Token("invalid.token.format");

        // when & then
        assertTrue(authenticator.isInvalidAuthentication(invalidToken));
    }

    @Test
    @DisplayName("유효한 토큰에서 Payload를 추출한다")
    void test5() {
        // given
        Payload originalPayload = Payload.from(memberId, role);
        Token token = authenticator.authenticate(originalPayload);

        // when
        Payload extractedPayload = authenticator.getPayload(token);

        // then
        assertThat(originalPayload).isEqualTo(extractedPayload);
    }

    @Test
    @DisplayName("잘못된 형식의 토큰에서 Payload를 추출하면 예외가 발생한다")
    void test6() {
        // given
        Token invalidToken = new Token("invalid.token.format");

        // when & then
        assertThatThrownBy(() -> authenticator.getPayload(invalidToken))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("다른 키로 서명된 토큰에서 Payload를 추출하면 예외가 발생한다")
    void test7() {
        // given
        String differentKey = "RGlmZmVyZW50U2VjcmV0S2V5Rm9yVGVzdGluZ0pXVEF1dGhlbnRpY2F0b3I=";
        SecretKey differentSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(differentKey));

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + validityInMilliseconds);

        String tokenWithDifferentKey = Jwts.builder()
                .setSubject(memberId)
                .claim(Authenticator.ROLE_CLAIM_EXPRESSION, role)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(differentSigningKey)
                .compact();

        // when & then
        assertThatThrownBy(() -> authenticator.getPayload(new Token(tokenWithDifferentKey)))
                .isInstanceOf(AuthorizationException.class);
    }
}
