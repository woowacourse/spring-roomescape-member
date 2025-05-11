package roomescape.business.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;

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
    private UserService sut;

    @Test
    void 사용자_등록이_성공적으로_이루어진다() {
        // given
        String name = "테스트유저";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existByEmail(email)).thenReturn(false);

        // when
        sut.register(name, email, password);

        // then
        verify(userRepository).existByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void 이미_존재하는_이메일로_사용자_등록_시_예외가_발생한다() {
        // given
        String name = "테스트유저";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existByEmail(email)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> sut.register(name, email, password))
                .isInstanceOf(InvalidCreateArgumentException.class);

        verify(userRepository).existByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void 이메일로_사용자를_조회할_수_있다() {
        // given
        String email = "test@example.com";
        User expectedUser = User.restore("user-id", "USER", "Test User", email, "password123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // when
        User result = sut.getByEmail(email);

        // then
        assertThat(result).isEqualTo(expectedUser);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void 존재하지_않는_이메일로_사용자_조회_시_예외가_발생한다() {
        // given
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> sut.getByEmail(email))
                .isInstanceOf(NotFoundException.class);

        verify(userRepository).findByEmail(email);
    }

    @Test
    void 모든_사용자를_조회할_수_있다() {
        // given
        List<User> expectedUsers = Arrays.asList(
                User.restore("user-id-1", "USER", "User One", "user1@example.com", "password1"),
                User.restore("user-id-2", "USER", "User Two", "user2@example.com", "password2")
        );

        when(userRepository.findAll()).thenReturn(expectedUsers);

        // when
        List<User> result = sut.getAll();

        // then
        assertThat(result).isEqualTo(expectedUsers);
        verify(userRepository).findAll();
    }
}
