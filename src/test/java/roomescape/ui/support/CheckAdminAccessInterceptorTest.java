package roomescape.ui.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.auth.Principal;
import roomescape.auth.exception.AccessDeniedException;
import roomescape.fixture.MemberFixture;

class CheckAdminAccessInterceptorTest {
    private final CheckAdminAccessInterceptor checkAdminAccessInterceptor = new CheckAdminAccessInterceptor();

    @Test
    void 관리자_페이지에_접근할_수_있으면_true를_반환한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Principal principal = Principal.from(MemberFixture.DEFAULT_ADMIN);
        request.setAttribute(AuthenticationExtractInterceptor.PRINCIPAL_KEY_NAME, principal);

        boolean isHandled = checkAdminAccessInterceptor.preHandle(request, null, null);

        assertThat(isHandled).isTrue();
    }

    @Test
    void 관리자_페이지에_접근할_수_없으면_예외가_발생한다() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Principal principal = Principal.from(MemberFixture.DEFAULT_MEMBER);
        request.setAttribute(AuthenticationExtractInterceptor.PRINCIPAL_KEY_NAME, principal);

        assertThatThrownBy(() -> checkAdminAccessInterceptor.preHandle(request, null, null))
                .isExactlyInstanceOf(AccessDeniedException.class);
    }
}
