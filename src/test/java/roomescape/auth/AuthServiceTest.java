package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.ServiceTest;
import roomescape.auth.exception.UnAuthorizedException;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@ServiceTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("토큰에서 id 정보를 가져온다.")
    void getIdFromTokenTest() {
        String token = tokenManager.createToken(new MemberRole(1L, "name", Role.MEMBER));
        long memberId = authService.getMemberId(token);
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("권한을 성공적으로 확인한다.")
    void validatePermissionTest() {
        String token = tokenManager.createToken(new MemberRole(1L, "name", Role.MEMBER));
        assertAll(
                () -> assertThatCode(() -> authService.validatePermission(token, Role.MEMBER)).doesNotThrowAnyException(),
                () -> assertThatCode(() -> authService.validatePermission(token, Role.ADMIN)).isInstanceOf(UnAuthorizedException.class)
        );
    }
}
