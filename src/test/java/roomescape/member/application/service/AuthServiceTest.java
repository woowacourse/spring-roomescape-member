package roomescape.member.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.member.InvalidMemberException;
import roomescape.member.application.dto.GetMemberResponse;
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
        Member member = authService.getUser(token.accessToken());

        // then
        assertThat(member.getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    void getMembersTest() {
        // given
        userRepository.insert(new Member(0L, "name1", "email1@email.com", "password", Role.USER));
        userRepository.insert(new Member(1L, "name2", "email2@email.com", "password", Role.USER));
        userRepository.insert(new Member(2L, "name3", "email3@email.com", "password", Role.USER));

        // when
        List<GetMemberResponse> members = authService.getMembers();

        // then
        assertThat(members).hasSize(3);
    }

}
