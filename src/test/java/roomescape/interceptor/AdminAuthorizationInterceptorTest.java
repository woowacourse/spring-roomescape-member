package roomescape.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.domain.entity.Role;
import roomescape.dto.LoginInfo;
import roomescape.error.AccessDeniedException;
import roomescape.infra.SessionLoginRepository;

class AdminAuthorizationInterceptorTest {

    private final SessionLoginRepository sessionLoginRepository = mock(SessionLoginRepository.class);
    private final AdminAuthorizationInterceptor sut = new AdminAuthorizationInterceptor(sessionLoginRepository);

    @DisplayName("관리자 권한이 있으면 true 를 반환한다")
    @Test
    void preHandle() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var session = request.getSession(true);

        when(sessionLoginRepository.getLoginInfo(session))
                .thenReturn(new LoginInfo(1L, "관리자", Role.ADMIN));

        // when
        var result = sut.preHandle(request, response, new Object());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("세션이 없는 경우 JSON 요청이면 예외를 던진다")
    @Test
    void preHandle_noSession_AccessDeniedException() {
        // given
        var request = new MockHttpServletRequest();
        request.addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        var response = new MockHttpServletResponse();

        // when // then
        assertThatThrownBy(() -> sut.preHandle(request, response, new Object()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("관리자 권한이 없습니다.");
    }

    @DisplayName("세션이 없는 경우 HTML 요청이면 /login 으로 리다이렉트된다")
    @Test
    void preHandle_noSession_redirectsToLogin() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        request.addHeader(ACCEPT, MediaType.TEXT_HTML_VALUE);
        var response = new MockHttpServletResponse();

        // when
        var result = sut.preHandle(request, response, new Object());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isFalse();
            softly.assertThat(response.getRedirectedUrl()).isEqualTo("/login");
        });
    }

    @DisplayName("권한이 없는 사용자의 경우 JSON요청 이면 예외를 던진다")
    @Test
    void preHandle_nonAdminUser_AccessDeniedException() {
        // given
        var request = new MockHttpServletRequest();
        request.addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        var response = new MockHttpServletResponse();
        var session = request.getSession(true);

        when(sessionLoginRepository.getLoginInfo(session))
                .thenReturn(new LoginInfo(1L, "유저", Role.USER));

        // when // then
        assertThatThrownBy(() -> sut.preHandle(request, response, new Object()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("관리자 권한이 없습니다.");
    }

    @DisplayName("권한이 없는 사용자의 경우 HTML 요청이면 /login 으로 리다이렉트된다")
    @Test
    void preHandle_nonAdminUser_redirectsToLogin() throws Exception {
        // given
        var request = new MockHttpServletRequest();
        request.addHeader(ACCEPT, MediaType.TEXT_HTML_VALUE);
        var response = new MockHttpServletResponse();
        var session = request.getSession(true);

        when(sessionLoginRepository.getLoginInfo(session))
                .thenReturn(new LoginInfo(1L, "유저", Role.USER));

        // when
        var result = sut.preHandle(request, response, new Object());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isFalse();
            softly.assertThat(response.getRedirectedUrl()).isEqualTo("/login");
        });
    }
}
