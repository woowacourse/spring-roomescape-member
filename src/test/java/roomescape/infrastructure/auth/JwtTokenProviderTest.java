package roomescape.infrastructure.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

class JwtTokenProviderTest {
    private static final String TEST_SECRET_KEY = "testSecretKey";

    @DisplayName("이메일로 토큰을 생성한다.")
    @Test
    void createTokenTest() {
        JwtTokenProvider jwtTokenProvider = createJwtTokenProvider(1000);
        String payload = "test@test.com";

        String token = jwtTokenProvider.createToken(payload);

        String subject = Jwts.parser()
                .setSigningKey(TEST_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(payload).isEqualTo(subject);
    }

    @DisplayName("만료된 토큰의 payload를 추출하면 예외가 발생한다.")
    @Test
    void getPayloadExceptionTest() {
        JwtTokenProvider jwtTokenProvider = createJwtTokenProvider(-1);
        String payload = "test@test.com";
        String expiredToken = jwtTokenProvider.createToken(payload);

        assertThatCode(() -> jwtTokenProvider.getPayload(expiredToken))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(RoomescapeErrorCode.TOKEN_EXPIRED);
    }

    private JwtTokenProvider createJwtTokenProvider(int validityInMilliseconds) {
        JwtTokenProperties jwtTokenProperties = new JwtTokenProperties();
        jwtTokenProperties.setSecretKey(TEST_SECRET_KEY);
        jwtTokenProperties.setValidityInMilliseconds(validityInMilliseconds);
        return new JwtTokenProvider(jwtTokenProperties);
    }
}
