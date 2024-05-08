package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.jwt.JwtTokenProvider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthServiceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("토큰을 생산하면 email을 기반으로 토큰이 생성된다.")
    void createToken() {
        //given
        String givenEmail = "email@email.com";
        LoginRequest loginRequest = new LoginRequest(givenEmail, "1234");

        //when
        LoginResponse loginResponse = authService.createToken(loginRequest);
        String result = jwtTokenProvider.getPayload(loginResponse.getAccessToken());

        //then
        assertThat(result).isEqualTo(givenEmail);
    }
}
