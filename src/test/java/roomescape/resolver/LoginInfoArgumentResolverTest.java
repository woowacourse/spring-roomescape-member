package roomescape.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import roomescape.domain.LoginSession;
import roomescape.domain.Role;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;

class LoginInfoArgumentResolverTest {

    private final LoginSession loginSession = mock(LoginSession.class);
    private final LoginInfoArgumentResolver sut = new LoginInfoArgumentResolver(loginSession);

    @Test
    @DisplayName("세션이 존재하면 loginSession을 통해 LoginInfo를 반환한다")
    void resolveArgument() {
        // given
        var request = new MockHttpServletRequest();
        var session = request.getSession();
        var webRequest = new ServletWebRequest(request);

        var expectedLoginInfo = new LoginInfo(1L, "홍길동", Role.USER);
        when(loginSession.getLoginInfo(session)).thenReturn(expectedLoginInfo);

        // when
        LoginInfo result = (LoginInfo) sut.resolveArgument(null, null, webRequest, null);

        // then
        assertThat(result).isEqualTo(expectedLoginInfo);
    }

    @Test
    @DisplayName("세션이 없으면 예외가 발생한다")
    void resolveArgument_noSession() {
        // given
        var request = new MockHttpServletRequest();
        var webRequest = new ServletWebRequest(request);

        // when // then
        assertThatThrownBy(() -> sut.resolveArgument(null, null, webRequest, null))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("세션이 존재하지 않습니다.");
    }
}
