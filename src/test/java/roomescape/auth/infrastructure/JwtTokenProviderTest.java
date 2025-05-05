package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;

    private final String rawSecretKey = Base64.getEncoder().encodeToString(
            "thisIsASecretKeyForHS256ThatIsAtLeast32ByteLong!".getBytes(StandardCharsets.UTF_8)
    );

    @BeforeEach
    void setUp() {
        long validityInMilliseconds = 60 * 60;
        jwtTokenProvider = new JwtTokenProvider(rawSecretKey, validityInMilliseconds);
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
