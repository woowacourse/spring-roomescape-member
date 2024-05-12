package roomescape.ui.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.application.AuthService;
import roomescape.auth.Principal;
import roomescape.auth.exception.AuthorizationException;
import roomescape.fixture.MemberFixture;

@ExtendWith(MockitoExtension.class)
class AuthenticationExtractInterceptorTest {
    @InjectMocks
    private AuthenticationExtractInterceptor authenticationExtractInterceptor;
    @Mock
    private AuthService authService;

    @Test
    void attribute에_principal_객체를_저장한다() {
        when(authService.createPrincipal(anyString())).thenReturn(Principal.from(MemberFixture.DEFAULT_MEMBER));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", "토"));

        authenticationExtractInterceptor.preHandle(request, null, null);

        Principal principal = (Principal) request.getAttribute(AuthenticationExtractInterceptor.PRINCIPAL_KEY_NAME);
        assertThat(principal.getId()).isEqualTo(1L);
    }

    @Test
    void 인증_정보를_담은_쿠키가_없으면_예외가_발생한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> authenticationExtractInterceptor.preHandle(request, null, null))
                .isExactlyInstanceOf(AuthorizationException.class);
    }
}
