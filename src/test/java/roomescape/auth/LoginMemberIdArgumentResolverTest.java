package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Clock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@ExtendWith(MockitoExtension.class)
class LoginMemberIdArgumentResolverTest {

    @Mock
    private ObjectProvider<RequestPayloadContext> contextProvider;

    @Mock
    NativeWebRequest nativeWebRequest;

    @Test
    @DisplayName("LoginMemberId가 올바르게 변환된다.")
    void resolveMemberIdTest() throws NoSuchMethodException {
        TokenManager tokenManager = new TokenManager("test".repeat(20), 10000, Clock.systemDefaultZone());
        given(contextProvider.getObject()).willReturn(new RequestPayloadContext(tokenManager));
        LoginMemberIdArgumentResolver resolver = new LoginMemberIdArgumentResolver(contextProvider);

        Method testMethod = this.getClass().getDeclaredMethod("testMethod", long.class);
        MethodParameter parameter = new MethodParameter(testMethod, 0);
        MockHttpServletRequest request = new MockHttpServletRequest();
        given(nativeWebRequest.getNativeRequest(HttpServletRequest.class)).willReturn(request);

        String token = tokenManager.createToken(new MemberRole(5L, "name", Role.MEMBER));
        request.setCookies(new Cookie("token", token));

        Long id = resolver.resolveArgument(parameter, null, nativeWebRequest, null);
        assertThat(id).isEqualTo(5L);
    }

    private void testMethod(@LoginMemberId long memberId) {
    }
}
