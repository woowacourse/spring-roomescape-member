package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.GetAuthInfoResponse;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.util.MemberFixture;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    AuthServiceTest() {
        this.tokenProvider = new TokenProvider("secretKey", 360000);
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
                .hasMessage("로그인하려는 계정이 존재하지 않습니다. 회원가입 후 로그인해주세요.");
    }

    @Test
    @DisplayName("유저 로그인 시 입력한 비밀번호가 계정의 비밀번호와 다를 경우, 예외가 발생한다.")
    void login_WhenMemberPasswordNotSame() {
        // given
        String email = "email@naver.com";
        String password = "asdf";
        memberRepository.save(MemberFixture.getOne(email, password));

        // when & then
        assertThatThrownBy(() -> authService.login(new LoginRequest(email, "hihi" + password)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호를 잘못 입력했습니다. 다시 입력해주세요.");
    }

    @Test
    @DisplayName("토큰으로부터 회원 정보를 찾아 반환한다.")
    void getMemberAuthInfo() {
        // given
        String email = "moly@hihi.com";
        Member member = memberRepository.save(MemberFixture.getOne(email));
        String token = tokenProvider.createToken(member);

        // when & then
        assertThat(authService.getMemberAuthInfo(token)).isEqualTo(GetAuthInfoResponse.from(member));
    }

    @Test
    @DisplayName("토큰으로부터 얻은 회원 정보가 존재하지 않는 경우, 예외를 반환한다.")
    void getMemberAuthInfo_WhenMemberNotExists() {
        // given
        String token = tokenProvider.createToken(MemberFixture.getOneWithId(1L));

        // when & then
        assertThatThrownBy(() -> authService.getMemberAuthInfo(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인하려는 계정이 존재하지 않습니다. 회원가입 후 로그인해주세요.");
    }
}
