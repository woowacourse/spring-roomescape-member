package roomescape.auth.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import roomescape.business.model.vo.LoginInfo;
import roomescape.exception.auth.NotAuthenticatedException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthorizationArgumentResolverTest {

    private AuthorizationArgumentResolver resolver;
    private MethodParameter parameter;
    private NativeWebRequest webRequest;

    @BeforeEach
    void setUp() {
        resolver = new AuthorizationArgumentResolver();
        parameter = mock(MethodParameter.class);
        webRequest = mock(NativeWebRequest.class);
    }

    @Test
    void LoginInfo_파라미터를_지원하면_true를_반환한다() {
        // given
        given(parameter.getParameterType()).willReturn((Class) LoginInfo.class);

        // when
        boolean result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void LoginInfo가_아닌_파라미터는_false를_반환한다() {
        // given
        given(parameter.getParameterType()).willReturn((Class) String.class);

        // when
        boolean result = resolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void authorization_속성이_있으면_해당_객체를_반환한다() {
        // given
        LoginInfo loginInfo = mock(LoginInfo.class);
        given(webRequest.getAttribute("authorization", RequestAttributes.SCOPE_REQUEST))
                .willReturn(loginInfo);

        // when
        Object result = resolver.resolveArgument(parameter, null, webRequest, null);

        // then
        assertThat(result).isEqualTo(loginInfo);
    }

    @Test
    void authorization_속성이_없으면_NotAuthenticatedException을_던진다() {
        // given
        given(webRequest.getAttribute("authorization", RequestAttributes.SCOPE_REQUEST))
                .willReturn(null);

        // when, then
        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, webRequest, null))
                .isInstanceOf(NotAuthenticatedException.class);
    }
}
