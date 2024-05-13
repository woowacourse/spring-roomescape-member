package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.member.service.LoginService;
import roomescape.member.dto.LoginRequest;
import roomescape.global.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Test
    void login() {
        LoginRequest request = new LoginRequest("tenny@wooteco.com", "1234");
        String token = loginService.login(request);

        assertThat(token).isNotBlank();
    }

    @Test
    void login_NotExistMember() {
        LoginRequest request = new LoginRequest("teni@wooteco.com", "1234");

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(RoomEscapeException.class);
    }
}
