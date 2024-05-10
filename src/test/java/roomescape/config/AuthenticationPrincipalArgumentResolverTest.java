package roomescape.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@ExtendWith(MockitoExtension.class)
class AuthenticationPrincipalArgumentResolverTest {

    @Mock
    private MethodParameter parameter;
    @Mock
    private ModelAndViewContainer mavContainer;
    @Mock
    private WebDataBinderFactory binderFactory;
    @Mock
    private NativeWebRequest nativeWebRequest;
    @Mock
    private MemberService memberService;
    @Mock
    private AuthService authService;

    @Test
    @DisplayName("요청에 쿠키가 존재하지 않으면 예외를 발생한다.")
    void resolveArgumentWhenNoCookies() {
        //given
        HandlerMethodArgumentResolver resolver =
                new AuthenticationPrincipalArgumentResolver(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        given(nativeWebRequest.getNativeRequest()).willReturn(request);

        //when //then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, nativeWebRequest, binderFactory))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("요청에 token 이름의 쿠키가 존재하지 않으면 예외를 발생한다.")
    void resolveArgumentNotExistTokenCookie() {
        //given
        HandlerMethodArgumentResolver resolver =
                new AuthenticationPrincipalArgumentResolver(memberService, authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("test", "test"));
        given(nativeWebRequest.getNativeRequest()).willReturn(request);

        //when //then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, nativeWebRequest, binderFactory))
                .isInstanceOf(AuthorizationException.class);
    }
}
