package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.member.LoginRequest;
import roomescape.exception.InvalidCredentialsException;
import roomescape.fixture.FakeMemberRepositoryFixture;
import roomescape.repository.FakeTokenProvider;
import roomescape.repository.MemberRepository;
import roomescape.util.CookieKeys;
import roomescape.util.TokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("사용자 로그인")
class LoginServiceTest {

    private final MemberRepository memberRepository = FakeMemberRepositoryFixture.create();
    private final TokenProvider tokenProvider = new FakeTokenProvider();
    private final LoginService loginService = new LoginService(memberRepository, new MemberService(memberRepository, tokenProvider), tokenProvider);

    @DisplayName("올바른 사용자 정보를 전달하면 JWT 토큰을 생성하여 쿠키에 저장한다")
    @Test
    void createLoginCookieTest() {
        // given
        LoginRequest request = new LoginRequest("wooteco7", "admin@gmail.com");

        // when
        Cookie cookie = loginService.createLoginCookie(request);
        String actual = cookie.getName();
        String expected = CookieKeys.TOKEN;

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("토큰 생성 시 사용자 정보가 잘못되면 예외가 발생한다")
    @Test
    void createLoginCookieExceptionTest() {
        // given
        LoginRequest request = new LoginRequest("", "admin@gmail.com");

        // when & then
        assertThatThrownBy(() -> loginService.createLoginCookie(request)).isInstanceOf(InvalidCredentialsException.class);
    }

    @DisplayName("로그아웃을 요청하면 쿠키의 max age 값을 0으로 설정한다")
    @Test
    void setLogoutCookieTest() {
        // when
        Cookie cookie = loginService.setLogoutCookie();

        // then
        assertThat(cookie.getAttribute("Max-Age")).isEqualTo("0");
    }
}
