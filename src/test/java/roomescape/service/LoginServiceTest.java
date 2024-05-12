package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.infrastructure.auth.TokenManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private MemberService memberService;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private CookieManager cookieManager;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("성공 : 아이디와 비밀번호가 일치하는 경우 로그인에 성공한다")
    void login() {
        //given
        Member dummy = new Member(1L, "콜리", "email@email.com", "password", "USER");
        Token token = new Token("token");
        Cookie cookie = new Cookie("madeCookie", "");
        LoginRequest loginRequest = new LoginRequest("password", "email@email.com");
        Mockito.when(memberService.findMemberByEmailAndPassword(anyString(), anyString()))
                .thenReturn(dummy);
        Mockito.when(tokenManager.generate(any(Member.class)))
                .thenReturn(token);
        Mockito.when(cookieManager.generate(token))
                .thenReturn(cookie);

        //when
        Cookie madeCookie = loginService.login(loginRequest);

        //then
        assertThat(madeCookie.getName()).isEqualTo(cookie.getName());
    }

    @Test
    @DisplayName("실패 : 아이디와 비밀번호가 일치하는 않으면 로그인에 실패한다")
    void should_ThrowIllegalArgumentException_WhenGiveInvalidInformation() {
        //given
        Member dummy = new Member(1L, "콜리", "22@email.com", "passdfword", "USER");
        LoginRequest loginRequest = new LoginRequest("password", "email@email.com");
        Mockito.when(memberService.findMemberByEmailAndPassword(anyString(), anyString()))
                .thenReturn(dummy);

        //when - then
        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void check() {
        Cookie[] cookies = new Cookie[1];
        Member dummy = new Member(1L, "콜리", "email@email.com", "password", "USER");
        Mockito.when(cookieManager.getToken(cookies))
                .thenReturn(new Token("token"));
        Mockito.when(tokenManager.getMemberId(any(Token.class)))
                .thenReturn(1L);
        Mockito.when(memberService.findMemberById(eq(1L)))
                .thenReturn(dummy);

        //when
        Member member = loginService.check(cookies);

        //then
        assertThat(member.getId()).isEqualTo(dummy.getId());
    }

    @Test
    void logOut() {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("token", "token");
        Mockito.when(cookieManager.makeResetCookie(cookies))
                .thenCallRealMethod();

        //when
        Cookie cookie = loginService.logOut(cookies);

        //then
        assertThat(cookie.getMaxAge()).isZero();
    }
}