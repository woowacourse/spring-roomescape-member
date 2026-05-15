package roomescape.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.support.DatabaseHelper;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;

import java.util.Optional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseHelper databaseHelper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        databaseHelper.cleanUp();
    }

    @Test
    void 유저를_생성하면_DB에_실제로_저장되다() {
        User user = userService.create("루크");

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("루크");
    }

    @Test
    void 기존_유저가_로그인하면_새로_생성하지_않고_조회한다() {
        User user = userService.getOrCreateUserByName("소낙눈");

        assertThat(user.getId()).isEqualTo(user.getId());
    }

    @Test
    void 새로운_유저가_로그인하면_DB에_새로_저장된다() {
        User user = userService.getOrCreateUserByName("피노");

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("피노");

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
    }
}
