package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.exception.AccessDeniedException;
import roomescape.domain.member.MemberRepository;
import roomescape.support.annotation.WithoutWebSpringBootTest;

@WithoutWebSpringBootTest
@Sql("/member.sql")
class CheckAdminAccessInterceptorTest {
    private final CheckAdminAccessInterceptor checkAdminAccessInterceptor;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    CheckAdminAccessInterceptorTest(AuthenticationInfoExtractor extractor, MemberRepository memberRepository) {
        checkAdminAccessInterceptor = new CheckAdminAccessInterceptor(extractor, memberRepository);
    }

    @Test
    void 관리자_페이지에_접근할_수_있으면_true를_반환한다() {
        String token = tokenProvider.createToken("1");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", token));

        boolean isHandled = checkAdminAccessInterceptor.preHandle(request, null, null);

        assertThat(isHandled).isTrue();
    }

    @Test
    void 관리자_페이지에_접근할_수_없으면_예외가_발생한다() {
        String token = tokenProvider.createToken("2");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", token));

        assertThatThrownBy(() -> checkAdminAccessInterceptor.preHandle(request, null, null))
                .isExactlyInstanceOf(AccessDeniedException.class);
    }
}
