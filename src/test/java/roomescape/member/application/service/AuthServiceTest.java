package roomescape.member.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.JwtTokenProvider;
import roomescape.member.infrastructure.fake.FakeMemberDao;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.TokenResponse;

class AuthServiceTest {

    private final AuthService authService;
    private final FakeMemberDao userRepository;

    AuthServiceTest() {
        this.userRepository = new FakeMemberDao();
        this.authService = new AuthService(new JwtTokenProvider("secret-key", 360000), userRepository);
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new Member(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        TokenResponse token = authService.login(loginRequest);

        // then
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    @DisplayName("유저 정보 조회 테스트")
    void getUserTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new Member(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, password);
        TokenResponse token = authService.login(loginRequest);

        // when
        Member member = authService.getUser(token.accessToken());

        // then
        assertThat(member.getName()).isEqualTo("name");
    }

}
