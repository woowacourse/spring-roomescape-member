package roomescape.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@ExtendWith(MockitoExtension.class)
class CheckAdminInterceptorTest {

    @Test
    @DisplayName("URI가 admin으로 시작하는데 회원 역할이 관리자이면 true와 200ok를 반환한다.")
    void preHandle() {
        //given
        CheckAdminInterceptor checkAdminInterceptor = new CheckAdminInterceptor(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/admin/**");
        String token = "1234";
        String payload = "test@test.com";
        request.setCookies(new Cookie("token", token));
        given(authService.findPayload(token)).willReturn(payload);
        given(memberService.findAuthInfo(payload)).willReturn(MemberFixtures.createAdminMember("daon", payload));

        //when
        boolean actual = checkAdminInterceptor.preHandle(request, response, any());
        int status = response.getStatus();

        //then
        assertAll(
                () -> assertThat(actual).isTrue(),
                () -> assertThat(status).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Mock
    private MemberService memberService;

    @Mock
    private AuthService authService;

    @Test
    @DisplayName("쿠키가 없는 경우  false와 401을 반환한다.")
    void preHandleWhenCookiesNull() {
        //given
        CheckAdminInterceptor checkAdminInterceptor = new CheckAdminInterceptor(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = checkAdminInterceptor.preHandle(request, response, any());
        int status = response.getStatus();

        //then
        assertAll(
                () -> assertThat(actual).isFalse(),
                () -> assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );

    }

    @Test
    @DisplayName("token에 해당하는 쿠키가 없는 경우 false와 401을 반환한다.")
    void preHandleWhenNotExistTokenCookie() {
        //given
        CheckAdminInterceptor checkAdminInterceptor = new CheckAdminInterceptor(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("anyString", "1234"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = checkAdminInterceptor.preHandle(request, response, any());
        int status = response.getStatus();

        //then
        assertAll(
                () -> assertThat(actual).isFalse(),
                () -> assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }

    @Test
    @DisplayName("URI가 admin으로 시작하고 회원 역할이 관리자가 아닐 경우 false와 401을 반환한다.")
    void preHandleWhenStartsWithAdminAndNotAdmin() {
        //given
        CheckAdminInterceptor checkAdminInterceptor = new CheckAdminInterceptor(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/admin/**");
        String token = "1234";
        String payload = "test@test.com";
        request.setCookies(new Cookie("token", token));
        given(authService.findPayload(token)).willReturn(payload);
        given(memberService.findAuthInfo(payload)).willReturn(MemberFixtures.createUserMember("daon", payload));

        //when
        boolean actual = checkAdminInterceptor.preHandle(request, response, any());
        int status = response.getStatus();

        //then
        assertAll(
                () -> assertThat(actual).isFalse(),
                () -> assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
        );
    }
}
