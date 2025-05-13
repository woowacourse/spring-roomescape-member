package roomescape.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.auth.application.exception.InvalidMemberException;
import roomescape.auth.presentation.JwtTokenProvider;
import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.auth.presentation.dto.TokenResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.fake.FakeMemberDao;

class AuthServiceTest {

    private final AuthService authService;
    private final FakeMemberDao userRepository;

    AuthServiceTest() {
        this.userRepository = new FakeMemberDao();
        this.authService = new AuthService(new JwtTokenProvider("secret-key", 360000), userRepository);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
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
    @DisplayName("이메일이 존재하지 않을 경우 로그인에 실패한다.")
    void notFoundEmailTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new Member(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest("notFound@email.com", password);

        // when - then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("이메일이 올바르지 않습니다.")
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("비밀번호가 틀릴 경우 로그인에 실패한다.")
    void loginFailedTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new Member(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, "wrong password");

        // when - then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("비밀번호가 틀렸습니다.")
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    void getMemberTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new Member(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, password);
        TokenResponse token = authService.login(loginRequest);

        // when
        Member member = authService.getMember(token.accessToken());

        // then
        assertThat(member.getName()).isEqualTo("name");
    }
}
