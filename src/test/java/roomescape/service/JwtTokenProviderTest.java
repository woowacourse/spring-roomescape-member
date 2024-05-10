package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("주어진 사용자로 토큰을 생성한다.")
    @Test
    void should_create_token_when_given_user() {
        User user = new User(1L, "썬", "sun@email.com", "1234");

        String token = jwtTokenProvider.createToken(user);

        assertThat(token).isNotBlank();
    }
}
