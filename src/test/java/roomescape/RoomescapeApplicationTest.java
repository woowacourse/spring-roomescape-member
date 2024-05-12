package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import roomescape.infrastructure.JwtTokenProvider;

class RoomescapeApplicationTest {

    @EnableConfigurationProperties(JwtTokenProvider.class)
    static class TestConfig {
    }

    @DisplayName("JwtTokenProvider 에 프로퍼티 값이 입력되는지 확인한다.")
    @Test
    void contextStartsWithValidProperties() {
        // given
        JwtTokenProvider testJwtTokenProvider = new JwtTokenProvider("secretKey", 1000L);
        String payload = "payload";

        // when & then
        // context 에서 꺼낸 JwtTokenProvider 빈과 testJwtTokenProvider 은 같은 payload 에 대해
        // 동일한 토큰 값을 반환해야 한다.
        contextRunner()
                .run(context -> {
                    JwtTokenProvider jwtTokenProvider = context.getBean(JwtTokenProvider.class);
                    String expectedToken = testJwtTokenProvider.createToken(payload);
                    String actualToken = jwtTokenProvider.createToken(payload);

                    assertThat(expectedToken).isEqualTo(actualToken);
                });
    }

    private ApplicationContextRunner contextRunner() {
        return new ApplicationContextRunner()
                .withUserConfiguration(TestConfig.class)
                .withPropertyValues("security.jwt.token.secret-key=secretKey",
                        "security.jwt.token.validity-in-milliseconds=1000");
    }
}
