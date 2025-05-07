package roomescape.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.business.DuplicatedEmailException;
import roomescape.exception.business.UserNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 등록이 성공적으로 이루어진다")
    void register_Success() {
        // given
        String name = "테스트유저";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existByEmail(email)).thenReturn(false);

        // when
        userService.register(name, email, password);

        // then
        verify(userRepository).existByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 사용자 등록 시 예외가 발생한다")
    void register_DuplicateEmail_ThrowsException() {
        // given
        String name = "테스트유저";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.register(name, email, password))
                .isInstanceOf(DuplicatedEmailException.class);

        verify(userRepository).existByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("이메일로 사용자를 조회할 수 있다")
    void getByEmail_ExistingEmail_ReturnsUser() {
        // given
        String email = "test@example.com";
        User expectedUser = User.afterSave("user-id", "USER", "Test User", email, "password123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // when
        User result = userService.getByEmail(email);

        // then
        assertThat(result).isEqualTo(expectedUser);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 조회 시 예외가 발생한다")
    void getByEmail_NonExistingEmail_ThrowsException() {
        // given
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("모든 사용자를 조회할 수 있다")
    void getAll_ReturnsAllUsers() {
        // given
        List<User> expectedUsers = Arrays.asList(
                User.afterSave("user-id-1", "USER", "User One", "user1@example.com", "password1"),
                User.afterSave("user-id-2", "USER", "User Two", "user2@example.com", "password2")
        );

        when(userRepository.findAll()).thenReturn(expectedUsers);

        // when
        List<User> result = userService.getAll();

        // then
        assertThat(result).isEqualTo(expectedUsers);
        verify(userRepository).findAll();
    }
}
