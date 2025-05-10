package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;
import roomescape.auth.AuthRequired;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.LoginInfo;
import roomescape.exception.auth.NotAuthenticatedException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AuthenticationInterceptorTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JwtUtil jwtUtil;
    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        jwtUtil = mock(JwtUtil.class);
        interceptor = new AuthenticationInterceptor(jwtUtil);
    }

    @Test
    void 인증이_필요하지_않은_핸들러_메소드는_true를_반환한다() throws Exception {
        // given
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        given(handlerMethod.getMethodAnnotation(AuthRequired.class)).willReturn(null);

        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 유효한_토큰으로_인증에_성공하면_true를_반환한다() throws Exception {
        // given
        String token = "valid.jwt.value";
        Cookie cookie = new Cookie("authToken", token);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        LoginInfo loginInfo = mock(LoginInfo.class);

        given(handlerMethod.getMethodAnnotation(AuthRequired.class)).willReturn(mock(AuthRequired.class));
        given(request.getCookies()).willReturn(new Cookie[]{cookie});
        given(jwtUtil.validateAndResolveToken(token)).willReturn(loginInfo);

        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
        verify(request).setAttribute("authorization", loginInfo);
    }

    @Test
    void 쿠키가_없으면_NotAuthenticatedException을_던진다() {
        // given
        HandlerMethod handlerMethod = mock(HandlerMethod.class);
        given(handlerMethod.getMethodAnnotation(AuthRequired.class)).willReturn(mock(AuthRequired.class));
        given(request.getCookies()).willReturn(null);
        given(jwtUtil.validateAndResolveToken(null)).willThrow(NotAuthenticatedException.class);

        // when, then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(NotAuthenticatedException.class);
    }

    @Test
    void 유효하지_않은_토큰으로_NotAuthenticatedException을_던진다() {
        // given
        String token = "invalid.jwt.value";
        Cookie cookie = new Cookie("authToken", token);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(handlerMethod.getMethodAnnotation(AuthRequired.class)).willReturn(mock(AuthRequired.class));
        given(request.getCookies()).willReturn(new Cookie[]{cookie});
        given(jwtUtil.validateAndResolveToken(token)).willThrow(NotAuthenticatedException.class);

        // when, then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(NotAuthenticatedException.class);
    }

    @Test
    void HandlerMethod가_아닌_handler는_true를_반환한다() throws Exception {
        // given
        Object handler = new Object();

        // when
        boolean result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }
}
