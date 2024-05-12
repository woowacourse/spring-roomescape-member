package roomescape.infrastructure.authentication;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.MemberName;
import roomescape.domain.MemberRole;
import roomescape.domain.Password;
import roomescape.infrastructure.persistence.MemberRepository;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticationFailException;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.service.auth.UnauthorizedException;

class JwtAuthServiceTest {

    private final MemberRepository repository;
    private final AuthService authService;

    public JwtAuthServiceTest() {
        this.repository = Mockito.mock(MemberRepository.class);
        this.authService = new JwtAuthService(repository, getProperties());
    }

    private JwtProperties getProperties() {
        JwtProperties properties = Mockito.mock(JwtProperties.class);
        when(properties.secretKey())
                .thenReturn(UUID.randomUUID().toString());

        return properties;
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 토큰을 발급할 수 없다.")
    void cantAuthentication() {
        when(repository.findByEmail(any()))
                .thenReturn(Optional.empty());
        AuthenticationRequest request = new AuthenticationRequest("email", "password");

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(AuthenticationFailException.class)
                .hasMessage("올바른 인증 정보를 입력해주세요.");
    }

    @Test
    @DisplayName("비밀번호가 틀리다면 사용자에게 토큰을 발급할 수 없다.")
    void cantAuthenticationWithWrongPassword() {
        Member member = new Member(
                new MemberName("아톰"),
                new Email("email"),
                new Password("password"), MemberRole.NORMAL
        );
        when(repository.findByEmail("email@test.com"))
                .thenReturn(Optional.of(member));
        AuthenticationRequest request = new AuthenticationRequest("email@test.com", "wrongPw");

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(AuthenticationFailException.class)
                .hasMessage("올바른 인증 정보를 입력해주세요.");
    }

    @Test
    @DisplayName("토큰 파싱이 실패하면, 인가를 할 수 없다.")
    void cantAuthorized() {
        assertThatThrownBy(() -> authService.authorize("invalid-token"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효한 인가 정보를 입력해주세요.");
    }
}
