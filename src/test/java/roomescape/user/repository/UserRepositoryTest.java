package roomescape.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
}
