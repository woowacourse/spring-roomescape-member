package roomescape.service;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(token).isNotBlank();
    }
}
