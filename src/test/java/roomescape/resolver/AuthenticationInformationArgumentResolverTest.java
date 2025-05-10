package roomescape.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.MemberRole;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.utility.CookieUtility;
import roomescape.utility.JwtTokenProvider;

class AuthenticationInformationArgumentResolverTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("test_secret_key", 5000L);
    private final CookieUtility cookieUtility = new CookieUtility();
    private final AuthenticationInformationArgumentResolver argumentResolver =
            new AuthenticationInformationArgumentResolver(jwtTokenProvider, cookieUtility);

    @DisplayName("요청 메세지의 쿠키에 담긴 Access 토큰으로 부터 인증정보를 파싱할 수 있다")
    @Test
    void canParseAuthenticationInformation() {
        // given
        MethodParameter parameter = mock(MethodParameter.class);
        ModelAndViewContainer mvContainer = mock(ModelAndViewContainer.class);
        WebDataBinderFactory webDataBinderFactory = mock(WebDataBinderFactory.class);

        String accessToken = jwtTokenProvider.makeAccessToken(1L, "member", MemberRole.GENERAL);
        Cookie[] cookies = {new Cookie("access", accessToken)};
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getCookies()).thenReturn(cookies);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(servletRequest);

        // when
        Object authInformation = argumentResolver.resolveArgument(
                parameter, mvContainer, webRequest, webDataBinderFactory);

        // then
        assertAll(
                () -> assertThat(authInformation instanceof AuthenticationInformation).isTrue(),
                () -> assertThat(((AuthenticationInformation) authInformation).id()).isEqualTo(1L),
                () -> assertThat(((AuthenticationInformation) authInformation).name()).isEqualTo("member"),
                () -> assertThat(((AuthenticationInformation) authInformation).role()).isEqualTo(MemberRole.GENERAL)
        );
    }
}
