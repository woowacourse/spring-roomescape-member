package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.IntegrationTest;
import roomescape.dto.request.LoginRequest;

class AuthServiceTest extends IntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("토큰을 생성한다.")
    void createToken() {
        doReturn("created_token").when(jwtTokenProvider).createToken(any());

        LoginRequest request = new LoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);

        String token = authService.createToken(request);

        assertThat(token).isEqualTo("created_token");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않을 경우 토큰을 생성할 수 없다.")
    void createToken_fail_when_password_not_matched() {
        LoginRequest request = new LoginRequest(ADMIN_EMAIL, "wrong_password");

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("이메일이 존재하지 않을 경우 토큰을 생성할 수 없다.")
    void createToken_fail_when_email_not_matched() {
        LoginRequest request = new LoginRequest("not_exist_email", ADMIN_PASSWORD);

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 이메일의 회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("토큰으로 회원 아이디를 가져올 수 있다.")
    void getMemberId() {
        doReturn(1L).when(jwtTokenProvider).getMemberId(any());

        Long memberId = authService.getMemberId("token");

        assertThat(memberId).isEqualTo(1L);
    }
}
