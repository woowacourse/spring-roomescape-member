package roomescape.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰을 생성할 수 있다.")
    void createToken() {
        final String token = jwtTokenProvider.createToken("email@email.com");

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("토큰을 분석할 수 있다.")
    void getEmail() {
        final String token = jwtTokenProvider.createToken("email@email.com");

        final String result = jwtTokenProvider.getEmail(token);

        assertThat(result).isEqualTo("email@email.com");
    }
}
