package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private final String secretKey = Base64.getEncoder().encodeToString(
            "test-secret-key-long-long-long-long-long".getBytes(StandardCharsets.UTF_8)
    );
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, 1000 * 60 * 60);
    }

    @Test
    void createTokenAndGetPayload_shouldBeDoneSuccessfully() {
        String payload = "user@email.com";

        String token = jwtTokenProvider.createToken(payload);
        String extractedPayload = jwtTokenProvider.getPayload(token);

        assertThat(extractedPayload).isEqualTo(payload);
    }
}
