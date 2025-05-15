package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.jwt.AuthTokenProvider;
import roomescape.auth.service.dto.CreateTokenServiceRequest;
import roomescape.exception.custom.AuthenticationException;
import roomescape.exception.custom.AuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthTokenProvider authTokenProvider;

    @DisplayName("로그인 시, 토큰을 생성한다")
    @Test
    void createTokenTest() {
        // given
        CreateTokenServiceRequest request =
                new CreateTokenServiceRequest("user@email.com", "password");

        // when
        String token = authService.createToken(request);

        // then
        assertThat(token).isNotBlank();
    }

    @DisplayName("잘못된 이메일로 로그인 시 예외가 발생한다")
    @Test
    void createTokenTest_WhenEmailIsWrong() {
        // given
        CreateTokenServiceRequest request =
                new CreateTokenServiceRequest("notfound@email.com", "password");

        // when // then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("존재하지 않는 이메일 입니다");
    }

    @DisplayName("토큰으로 회원 정보를 조회할 수 있다")
    @Test
    void findMemberByTokenTest() {
        // given
        CreateTokenServiceRequest request = new CreateTokenServiceRequest("admin@email.com", "password");
        String token = authService.createToken(request);

        // when
        Member result = authService.findMemberByToken(token);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getEmail()).isEqualTo("admin@email.com")
        );
    }

    @DisplayName("토큰 없이 회원 조회 시 예외를 던진다")
    @Test
    void findMemberByTokenTest_WhenTokenNotExists() {
        assertThatThrownBy(() -> authService.findMemberByToken(" "))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("로그인 토큰이 존재하지 않습니다");
    }

    @DisplayName("토큰이 ADMIN 역할을 가진 경우 true를 반환한다")
    @Test
    void isAdminTest_WhenTokenHasAdminRole() {
        // given
        String token = authTokenProvider.createTokenFromMember(
                new Member(1L, Role.ADMIN, "어드민", "admin@email.com", "password")
        );

        // when
        boolean result = authService.isAdmin(token);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("토큰이 USER 역할을 가진 경우 false를 반환한다")
    @Test
    void isAdminTest_WhenTokenHasUserRole() {
        // given
        String token = authTokenProvider.createTokenFromMember(
                new Member(2L, Role.USER, "사용자", "user@email.com", "password")
        );

        // when
        boolean result = authService.isAdmin(token);

        // then
        assertThat(result).isFalse();
    }
}
