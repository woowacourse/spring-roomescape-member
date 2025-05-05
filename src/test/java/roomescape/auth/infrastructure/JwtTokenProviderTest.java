package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();

        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        injectPrivateField(jwtTokenProvider, "secretKey", encodedKey);
        injectPrivateField(jwtTokenProvider, "validityInMilliseconds", 60 * 60);
    }

    @DisplayName("JWT를 생성하고 다시 파싱하면 payload가 동일하다")
    @Test
    void createAndParseToken() {
        String payload = "test@email.com";
        String token = jwtTokenProvider.createToken(payload);

        assertThat(jwtTokenProvider.getPayload(token)).isEqualTo(payload);
    }

    @DisplayName("JWT를 생성하면 유효하다")
    @Test
    void validateToken_true_when_valid() {
        String token = jwtTokenProvider.createToken("test");

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @DisplayName("잘못된 토큰은 유효하지 않다")
    @Test
    void validateToken_false_when_invalid() {
        String fakeToken = "invalid.token.value";

        assertThat(jwtTokenProvider.validateToken(fakeToken)).isFalse();
    }

    private void injectPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("필드 주입 실패: " + fieldName, e);
        }
    }

}
