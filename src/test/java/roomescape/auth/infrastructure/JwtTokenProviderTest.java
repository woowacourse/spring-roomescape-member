package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Role;

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
        long id = 1L;
        String payload = String.valueOf(id);
        String token = jwtTokenProvider.createToken(payload, Role.USER);

        assertThat(jwtTokenProvider.getMemberId(token)).isEqualTo(id);
    }

    @DisplayName("JWT를 생성하면 유효하다")
    @Test
    void validateToken_true_when_valid() {
        String token = jwtTokenProvider.createToken("test", Role.USER);

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @DisplayName("잘못된 토큰은 유효하지 않다")
    @Test
    void validateToken_false_when_invalid() {
        String fakeToken = "invalid.token.value";

        assertThat(jwtTokenProvider.validateToken(fakeToken)).isFalse();
    }
}
