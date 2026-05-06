package roomescape.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.user.model.Role;
import roomescape.user.model.User;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 새로운_유저를_저장하고_생성된_ID를_반환한다() {
        User user = new User("루크", Role.USER);

        Long id = userRepository.create(user);

        assertThat(id).isNotNull();
        assertThat(id).isPositive();
    }

    @Test
    void ID로_유저를_조회할_수_있다() {
        User user = new User("루크", Role.USER);
        Long savedId = userRepository.create(user);

        User foundUser = userRepository.findById(savedId);

        assertThat(foundUser.getId()).isEqualTo(savedId);
        assertThat(foundUser.getName()).isEqualTo("루크");
        assertThat(foundUser.getRole()).isEqualTo(Role.USER);
    }
}
