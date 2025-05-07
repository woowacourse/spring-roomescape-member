package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;
import roomescape.auth.Role;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.model.vo.UserRole;
import roomescape.exception.auth.ForbiddenException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthorizationInterceptorTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HandlerMethod handlerMethod;
    private JwtUtil jwtUtil;
    private AuthorizationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handlerMethod = mock(HandlerMethod.class);
        jwtUtil = mock(JwtUtil.class);
        interceptor = new AuthorizationInterceptor(jwtUtil);
    }

    @Test
    void HandlerMethod가_아닌_경우_true를_반환한다() throws Exception {
        // given
        Object handler = new Object();

        // when
        boolean result = interceptor.preHandle(request, response, handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void Role_어노테이션이_없는_경우_true를_반환한다() throws Exception {
        // given
        given(handlerMethod.getMethodAnnotation(Role.class)).willReturn(null);

        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 허용된_역할의_사용자는_true를_반환한다() throws Exception {
        // given
        String token = "valid.value";
        Cookie cookie = new Cookie("authToken", token);
        Role role = createRoleAnnotation(UserRole.ADMIN);
        LoginInfo loginInfo = new LoginInfo("test@test.com", UserRole.ADMIN);

        given(handlerMethod.getMethodAnnotation(Role.class)).willReturn(role);
        given(request.getCookies()).willReturn(new Cookie[]{cookie});
        given(jwtUtil.resolveToken(token)).willReturn(loginInfo);

        // when
        boolean result = interceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
        verify(request).setAttribute("authorization", loginInfo);
    }

    @Test
    void 허용되지_않은_역할의_사용자는_ForbiddenException을_던진다() throws Exception {
        // given
        String token = "valid.value";
        Cookie cookie = new Cookie("authToken", token);
        Role role = createRoleAnnotation(UserRole.ADMIN);
        LoginInfo loginInfo = new LoginInfo("test@test.com", UserRole.USER);

        given(handlerMethod.getMethodAnnotation(Role.class)).willReturn(role);
        given(request.getCookies()).willReturn(new Cookie[]{cookie});
        given(jwtUtil.resolveToken(token)).willReturn(loginInfo);

        // when, then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void 쿠키가_없는_경우_null을_반환한다() throws Exception {
        // given
        Role role = createRoleAnnotation(UserRole.ADMIN);
        given(handlerMethod.getMethodAnnotation(Role.class)).willReturn(role);
        given(request.getCookies()).willReturn(null);

        // when, then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void authToken_쿠키가_없는_경우_null을_반환한다() throws Exception {
        // given
        Role role = createRoleAnnotation(UserRole.ADMIN);
        Cookie cookie = new Cookie("otherCookie", "value");

        given(handlerMethod.getMethodAnnotation(Role.class)).willReturn(role);
        given(request.getCookies()).willReturn(new Cookie[]{cookie});

        // when, then
        assertThatThrownBy(() -> interceptor.preHandle(request, response, handlerMethod))
                .isInstanceOf(NullPointerException.class);
    }

    private Role createRoleAnnotation(UserRole... roles) {
        return new Role() {
            @Override
            public Class<Role> annotationType() {
                return Role.class;
            }

            @Override
            public UserRole[] value() {
                return roles;
            }
        };
    }
}
