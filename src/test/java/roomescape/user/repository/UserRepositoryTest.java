package roomescape.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.util.Optional;

@JdbcTest
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(jdbcTemplate);

        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM \"USER\"");
    }

    @Test
    void 새로운_유저를_저장하고_생성된_ID를_반환한다() {
        // given
        User user = new User("새로운유저", Role.USER);

        Long id = userRepository.create(user);

        assertThat(id).isNotNull();
        assertThat(id).isPositive();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"USER\" WHERE id = ?", Integer.class, id);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 이름으로_유저를_조회하면_유저를_반환한다() {
        // given
        User user = new User("조회유저", Role.USER);
        Long savedId = userRepository.create(user);

        // when
        Optional<User> foundUser = userRepository.findByName("조회유저");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedId);
        assertThat(foundUser.get().getName()).isEqualTo("조회유저");
    }

    @Test
    void 존재하지_않는_이름으로_유저를_조회하면_empty를_반환한다() {
        // given
        String nonExistingName = "없는유저";

        // when
        Optional<User> foundUser = userRepository.findByName(nonExistingName);

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    void 중복된_이름으로_유저를_생성하면_예외가_발생한다() {
        // given
        User user1 = new User("중복테스트", Role.USER);
        userRepository.create(user1); // 첫 번째 유저 생성

        User user2 = new User("중복테스트", Role.USER);

        // when & then
        assertThatThrownBy(() -> userRepository.create(user2))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
