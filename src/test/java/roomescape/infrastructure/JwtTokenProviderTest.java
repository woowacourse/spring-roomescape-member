package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(new JwtProperties("secretKey", 1000L));
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("payload");

        assertThat(token).isNotNull();
    }

    @DisplayName("토큰을 파싱하여 payload를 반환한다.")
    @Test
    void getPayload() {
        // given
        String token = jwtTokenProvider.createToken("payload");

        // when
        String payload = jwtTokenProvider.getPayload(token);

        // then
        assertThat(payload).isEqualTo("payload");
    }

    @DisplayName("빈 토큰이 입력되면 예외가 발생된다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void getPayloadWithEmptyToken(String token) {
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(token))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("만료된 토큰에 대해 검증한다.")
    @Test
    void isValidToken() throws InterruptedException {
        // given
        String token = jwtTokenProvider.createToken("payload");

        // when
        Thread.sleep(1000L);

        // then
        assertThat(jwtTokenProvider.isValidToken(token)).isFalse();
    }
}
