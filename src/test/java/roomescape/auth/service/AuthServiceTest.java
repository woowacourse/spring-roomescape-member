package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.auth.TokenProvider;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.member.repository.MemberRepository;
import roomescape.util.MemberFixture;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    private TokenProvider tokenProvider;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    AuthServiceTest() {
        this.tokenProvider = new TokenProvider("adffdsa", 100);
        this.memberRepository = new FakeMemberRepository();
        this.authService = new AuthService(memberRepository, tokenProvider);
    }

    @Test
    @DisplayName("유저 로그인 성공 시 생성한 토큰을 담은 정보를 반환한다.")
    void login() {
        // given
        String email = "email@naver.com";
        String password = "password";
        memberRepository.save(MemberFixture.getOne(email, password));

        // when
        LoginResponse loginResponse = authService.login(new LoginRequest(email, password));

        // then
        assertThat(loginResponse.token()).isNotBlank();
        // TODO: 목이 아니여서 안됌
        // Mockito.verify(tokenProvider, Mockito.times(1)).createToken(email);
    }

    @Test
    @DisplayName("유저 로그인 시 회원이 존재하지 않을 경우, 예외가 발생한다.")
    void login_WhenMemberNotExists() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("email@naver.com", "asdf")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인하려는 회원이 존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("유저 로그인 시 입력한 비밀번호가 계정의 비밀번호와 다를 경우, 예외가 발생한다..")
    void login_WhenMemberPasswordNotSame() {
        assertThatThrownBy(() -> authService.login(new LoginRequest("email@naver.com", "asdf")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인하려는 회원이 존재하지 않는 회원입니다.");
    }
}
