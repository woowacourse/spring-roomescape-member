package roomescape.intercept;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.MemberRole;
import roomescape.exception.ForbiddenException;
import roomescape.utility.CookieUtility;
import roomescape.utility.JwtTokenProvider;

class NotAdminBlockInterceptTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("test_secret_key", 5000L);
    private final CookieUtility cookieUtility = new CookieUtility();
    private final NotAdminBlockIntercept intercept = new NotAdminBlockIntercept(jwtTokenProvider, cookieUtility);

    @DisplayName("일반 요청을 경우 통과")
    @Test
    void canPassGeneralRequest() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "member", MemberRole.GENERAL);
        Cookie[] cookies = {new Cookie("access", accessToken)};
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(request.getRequestURI()).thenReturn("/general/request");

        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        boolean isPassed = intercept.preHandle(request, response, new Object());

        // then
        assertThat(isPassed).isTrue();
    }

    @DisplayName("관리자가 관리자 요청을 할 경우 통과")
    @Test
    void canPassAdminRequestByAdmin() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "member", MemberRole.ADMIN);
        Cookie[] cookies = {new Cookie("access", accessToken)};
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(request.getRequestURI()).thenReturn("/admin/request");

        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        boolean isPassed = intercept.preHandle(request, response, new Object());

        // then
        assertThat(isPassed).isTrue();
    }

    @DisplayName("일반 사용자가 관리자 요청을 할 경우 블록")
    @Test
    void cannotPassAdminRequestByGeneralMember() {
        // given
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "member", MemberRole.GENERAL);
        Cookie[] cookies = {new Cookie("access", accessToken)};
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(request.getRequestURI()).thenReturn("/admin/request");

        HttpServletResponse response = mock(HttpServletResponse.class);

        // when & then
        assertThatThrownBy(() -> intercept.preHandle(request, response, new Object()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("[ERROR] 접근권한이 존재하지 않습니다.");
    }
}
