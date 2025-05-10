package roomescape.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("jwt access token을 생성한다")
    void generateAccessToken() {
        // when
        String token = jwtTokenProvider.createAccessToken("hello");

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("jwt access token으로부터 payload를 추출한다")
    void extractPayload() {
        // given
        String token = jwtTokenProvider.createAccessToken("hello");

        // when
        String payload = jwtTokenProvider.getPayload(token);

        // then
        assertThat(payload).isEqualTo("hello");
    }
}