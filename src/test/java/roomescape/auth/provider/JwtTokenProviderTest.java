package roomescape.auth.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import roomescape.auth.domain.Token;

class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenProviderTest() {
        this.jwtTokenProvider = new JwtTokenProvider();
    }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "dkanrjskwjrdjqhrpTtmqslekejejrlfdjdigksmsrnsdy=");
        ReflectionTestUtils.setField(jwtTokenProvider, "expirationTime", 1000);
    }

    @Test
    @DisplayName("Token을 정상적으로 만든다.")
    void getAccessToken() {
        Token token = jwtTokenProvider.getAccessToken(1);
        assertNotNull(token.getToken());
    }
}
