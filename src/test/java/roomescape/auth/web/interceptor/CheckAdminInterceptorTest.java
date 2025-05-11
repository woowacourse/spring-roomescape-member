package roomescape.auth.web.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.auth.infrastructure.TokenService;
import roomescape.auth.web.constant.AuthConstant;
import roomescape.domain.member.model.Role;

@ExtendWith(MockitoExtension.class)
class CheckAdminInterceptorTest {

    @InjectMocks
    private CheckAdminInterceptor interceptor;

    @Mock
    private TokenService tokenService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("admin 권한을 가진 사용자가 로그인한 상태라면, true를 반환한다")
    @Test
    void adminSuccess() throws Exception {
        // given
        String authCookieKey = AuthConstant.AUTH_COOKIE_KEY;
        String token = "adminToken";
        Cookie cookie = new Cookie(authCookieKey, token);
        request.setCookies(cookie);

        AuthenticatedMember authenticatedMember = new AuthenticatedMember(1L, "email", "어드민", Role.ADMIN);
        given(tokenService.resolveAuthenticatedMember(token)).willReturn(authenticatedMember);

        // when
        boolean isPass = interceptor.preHandle(request, response, null);

        // then
        assertThat(isPass).isTrue();
    }

    @DisplayName("admin 권한이 없는 사용자가 접근하면, false를 반환한다")
    @Test
    void userFail() throws Exception {
        // given
        String authCookieKey = AuthConstant.AUTH_COOKIE_KEY;
        String token = "userToken";
        Cookie cookie = new Cookie(authCookieKey, token);
        request.setCookies(cookie);

        AuthenticatedMember authenticatedMember = new AuthenticatedMember(1L, "email", "어드민", Role.USER);
        given(tokenService.resolveAuthenticatedMember(token)).willReturn(authenticatedMember);

        // when
        boolean isPass = interceptor.preHandle(request, response, null);

        // then
        assertThat(isPass).isFalse();
    }
}
