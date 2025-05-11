package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.MemberRole;
import roomescape.exception.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckAdminInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    private CookieProvider cookieProvider;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private CheckAdminInterceptor checkAdminInterceptor;

    @Test
    void Role이_ADMIN이면_true를_리턴한다() throws Exception {
        //given
        Cookie[] cookies = {new Cookie("token", "admin_cookie")};
        String token = "admin_cookie";

        //when
        when(request.getCookies()).thenReturn(cookies);
        when(cookieProvider.extractTokenFromCookies(cookies)).thenReturn(token);
        when(jwtTokenProvider.extractMemberRoleFromToken(token)).thenReturn(MemberRole.ADMIN);
        boolean result = checkAdminInterceptor.preHandle(request, response, handler);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void Role이_ADMIN이_아니면_예외를_던진다() throws Exception {
        //given
        Cookie[] cookies = {new Cookie("token", "not_admin_cookie")};
        String token = "not_admin_cookie";

        //when
        when(request.getCookies()).thenReturn(cookies);
        when(cookieProvider.extractTokenFromCookies(cookies)).thenReturn(token);
        when(jwtTokenProvider.extractMemberRoleFromToken(token)).thenReturn(MemberRole.USER);

        //then
        assertThatThrownBy(() -> checkAdminInterceptor.preHandle(request, response, handler))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("접근 권한이 필요합니다.");
    }
}