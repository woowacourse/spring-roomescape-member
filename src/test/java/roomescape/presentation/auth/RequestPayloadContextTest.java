package roomescape.presentation.auth;

import static org.assertj.core.api.Assertions.assertThatCode;

import jakarta.servlet.http.Cookie;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import exception.UnAuthorizedException;
import roomescape.application.auth.JwtTokenManager;
import roomescape.application.auth.TokenManager;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

class RequestPayloadContextTest {
    private final TokenManager tokenManager = new JwtTokenManager("test".repeat(20), 10000, Clock.systemDefaultZone());

    private RequestPayloadContext context;

    @BeforeEach
    void setUp() {
        context = new RequestPayloadContext(tokenManager);
    }

    @Test
    @DisplayName("값이 설정되지 않았을 때 가져오려는 경우 예외를 발생한다.")
    void exceptionOnNullMemberRole() {
        assertThatCode(context::getMemberId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("인증에 오류가 발생했습니다.");
    }

    @Test
    @DisplayName("값이 설정되지 않았을 때, 권한을 설정하려는 경우 예외를 발생한다.")
    void exceptionOnPermissionCheckWithNullMemberRole() {
        assertThatCode(() -> context.validatePermission(Role.MEMBER))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("권한을 검증한다.")
    void validatePermissionTest() {
        String memberToken = tokenManager.createToken(new MemberRole(1L, "name", Role.MEMBER));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", memberToken));
        context.setMemberRoleIfNotPresent(request);

        assertThatCode(() -> context.validatePermission(Role.ADMIN))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
