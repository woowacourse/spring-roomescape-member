package roomescape.infrastructure.authentication;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.infrastructure.persistence.MemberRepository;

class AuthServiceTest {

    private final MemberRepository repository = Mockito.mock(MemberRepository.class);
    private final AuthService authService = new AuthService(repository);

    @Test
    @DisplayName("존재하지 않는 사용자에게 토큰을 발급할 수 없다.")
    void cantAuthentication() {
        when(repository.findByEmail(any()))
                .thenReturn(Optional.empty());
        AuthenticationRequest request = new AuthenticationRequest("email", "password");

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 인증 정보를 입력해주세요.");
    }

    @Test
    @DisplayName("토큰이 실패하면, 인가를 할 수 없다.")
    void cantAuthorized() {
        assertThatThrownBy(() -> authService.authorize("invalid-token"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("유효한 인가 정보를 입력해주세요.");
    }
}
