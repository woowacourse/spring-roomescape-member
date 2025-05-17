package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.User;
import roomescape.exception.UserNotFoundException;
import roomescape.fake.FakeUserDao;
import roomescape.presentation.dto.UserResponse;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new FakeUserDao());
    }

    @DisplayName("유저를 조회한다.")
    @Test
    void find() {
        // when
        final User actual = userService.find(1L);

        // then
        assertThat(actual.getName()).isEqualTo("hotteok");
    }

    @DisplayName("조회하려는 유저 id가 없다면 예외가 발생한다.")
    @Test
    void findOrThrowIfIdNotExists() {
        // given
        final Long id = 3L;

        // when & then
        assertThatThrownBy(() -> userService.find(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("모든 유저를 조회한다.")
    @Test
    void findAll() {
        // when
        final List<UserResponse> actual = userService.findAll();

        // then
        assertThat(actual).extracting("name")
                .containsExactly("hotteok", "gugu");
    }
}
