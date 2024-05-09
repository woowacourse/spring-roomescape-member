package roomescape.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.exception.NotAuthenticatedException;
import roomescape.member.domain.MemberRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestFixture.*;

@Sql("/test-schema.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("실제 사용자의 비밀번호와 입력 비밀번호가 일치하지 않으면 토큰을 생성할 수 없다.")
    void createTokenWithInvalidEmail() {
        // given
        memberRepository.save(USER_MIA());

        String invalidPassword = "invalid";
        LoginRequest loginRequest = new LoginRequest(MIA_EMAIL, invalidPassword);

        // when & then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(NotAuthenticatedException.class);
    }

    @Test
    @DisplayName("이메일을 가진 사용자가 존재하지 않으면 토큰을 생성할 수 없다.")
    void findByEmail() {
        // given
        String notExistingEmail = "notExistingEmail@google.com";
        LoginRequest loginRequest = new LoginRequest(notExistingEmail, TEST_PASSWORD);

        // when & then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(NotAuthenticatedException.class);
    }
}
