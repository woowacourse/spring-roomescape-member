package roomescape.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import exception.UnAuthorizedException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.application.auth.TokenManager;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@ExtendWith(MockitoExtension.class)
class AdminRoleInterceptorTest {
    @Mock
    private ObjectProvider<CredentialContext> contextProvider;

    @Mock
    private TokenManager tokenManager;

    @Test
    @DisplayName("인증되지 않은 사용자의 경우, admin 페이지에 접근 시 403 응답을 반환한다.")
    void unAuthorizedOnNotAdminInterceptorTest() {
        given(contextProvider.getObject()).willReturn(new CredentialContext());
        given(tokenManager.extract(any())).willReturn(new MemberRole(1L, "name", Role.MEMBER));
        AdminRoleInterceptor interceptor = new AdminRoleInterceptor(tokenManager, contextProvider);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie cookie = new Cookie("token", "invalid-token");
        request.setCookies(cookie);

        assertThatCode(() -> interceptor.preHandle(request, response, null))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("인증된 사용자의 경우, admin 페이지에 접근할 수 있다.")
    void authorizedInterceptorTest() {
        given(contextProvider.getObject()).willReturn(new CredentialContext());
        given(tokenManager.extract(any())).willReturn(new MemberRole(1L, "name", Role.ADMIN));
        AdminRoleInterceptor interceptor = new AdminRoleInterceptor(tokenManager, contextProvider);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie cookie = new Cookie("token", "valid-token");
        request.setCookies(cookie);

        boolean actual = interceptor.preHandle(request, response, null);
        assertThat(actual).isTrue();
    }
}
