package roomescape.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.user.dto.UserRequest;
import roomescape.user.dto.UserResponse;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("새로운 유저를 생성하면 UserResponse를 반환한다.")
    void createNewUserReturnsUserResponse() {
        // given
        UserRequest request = new UserRequest("루크");
        Long expectedId = 1L;
        User expectedUser = new User(expectedId, "루크", Role.USER);

        when(userRepository.create(any(User.class))).thenReturn(expectedId);

        // when
        UserResponse response = userService.create(request);

        // then
        assertThat(response.getId()).isEqualTo(expectedId);
        assertThat(response.getName()).isEqualTo("루크");
        verify(userRepository).create(any(User.class));
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 유저를 조회하면 예외가 발생한다.")
    void findByNonExistingNameThrowsException() {
        // given
        String nonExistingName = "없는유저";
        when(userRepository.findByName(nonExistingName)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findByName(nonExistingName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가입되지 않은 유저입니다. 로그인을 다시 확인해주세요.");
        verify(userRepository).findByName(nonExistingName);
    }
}
