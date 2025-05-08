package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "security.jwt.token.secret-key=your-secret-key",
        "security.jwt.token.expire-length=3600000"
})
@SpringBootTest(classes = JwtProvider.class)
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("토큰을 정상적으로 생성한다")
    void test1() {
        // given
        String email = "example@gmail.com";

        // when
        String token = jwtProvider.createToken(email);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("토큰에 담긴 정보가 정상적으로 파싱된다")
    void test2() {
        // given
        String email = "example@gmail.com";
        String token = jwtProvider.createToken(email);

        // when
        String payload = jwtProvider.getPayload(token);

        // then
        assertThat(payload).isEqualTo(email);
    }
}
