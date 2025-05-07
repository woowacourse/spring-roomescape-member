package roomescape.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.business.model.vo.Authentication;
import roomescape.exception.auth.LoginFailException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("올바른 이메일과 비밀번호로 인증에 성공한다")
    void authenticate_ValidCredentials_ReturnsAuthentication() {
        // given
        String email = "test@example.com";
        String password = "password123";
        User user = User.afterSave("user-id", "USER", "Test User", email, password);
        Authentication expectedAuth = mock(Authentication.class);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.getAuthentication(user)).thenReturn(expectedAuth);

        // when
        Authentication result = authService.authenticate(email, password);

        // then
        assertThat(result).isEqualTo(expectedAuth);
        verify(userRepository).findByEmail(email);
        verify(jwtUtil).getAuthentication(user);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 인증 시 예외가 발생한다")
    void authenticate_NonExistingEmail_ThrowsException() {
        // given
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.authenticate(email, password))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 인증 시 예외가 발생한다")
    void authenticate_InvalidPassword_ThrowsException() {
        // given
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = User.afterSave("user-id", "USER", "Test User", email, "correctPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> authService.authenticate(email, password))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(jwtUtil);
    }
}
