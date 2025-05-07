package roomescape.infrastructure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.model.entity.User;
import roomescape.test_util.JdbcTestUtil;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(JdbcUserRepository.class)
class JdbcUserRepositoryTest {

    private final JdbcUserRepository sut;
    private final JdbcTestUtil testUtil;

    @Autowired
    public JdbcUserRepositoryTest(final JdbcUserRepository sut, final JdbcTemplate jdbcTemplate) {
        this.sut = sut;
        this.testUtil = new JdbcTestUtil(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        testUtil.deleteAll();
    }

    @Test
    void 사용자를_저장하고_조회할_수_있다() {
        // given
        final User user = User.beforeSave("테스트유저", "test@example.com", "password123");

        // when
        final User result = sut.saveAndGet(user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("테스트유저");
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.role()).isEqualTo("USER");
    }

    @Test
    void 모든_사용자를_조회할_수_있다() {
        // given
        final long id1 = testUtil.insertUser("유저일");
        final long id2 = testUtil.insertUser("유저이");

        // when
        final List<User> result = sut.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(id1);
        assertThat(result.get(1).id()).isEqualTo(id2);
    }

    @Test
    void ID로_사용자를_조회할_수_있다() {
        // given
        final long id = testUtil.insertUser("유저일");

        // when
        final Optional<User> result = sut.findById(id);

        // then
        assertThat(result).isPresent();
        final User user = result.get();
        assertThat(user.id()).isEqualTo(id);
        assertThat(user.name()).isEqualTo("유저일");
        assertThat(user.email()).isEqualTo("유저일@email.com");
    }

    @Test
    void 존재하지_않는_ID로_사용자를_조회하면_빈_Optional을_반환한다() {
        // when
        final Optional<User> result = sut.findById(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 이메일로_사용자를_조회할_수_있다() {
        // given
        final String name = "유저일";
        final String email = name + "@email.com";
        testUtil.insertUser(name);

        // when
        final Optional<User> result = sut.findByEmail(email);

        // then
        assertThat(result).isPresent();
        final User user = result.get();
        assertThat(user.name()).isEqualTo(name);
        assertThat(user.email()).isEqualTo(email);
    }

    @Test
    void 존재하지_않는_이메일로_사용자를_조회하면_빈_Optional을_반환한다() {
        // when
        final Optional<User> result = sut.findByEmail("nonexistent@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 이메일이_존재하는지_확인할_수_있다() {
        // given
        final String name = "유저1";
        final String email = name + "@email.com";
        testUtil.insertUser(name);

        // when
        final boolean result = sut.existByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 존재하지_않는_이메일로_확인하면_false를_반환한다() {
        // when
        final boolean result = sut.existByEmail("nonexistent@email.com");

        // then
        assertThat(result).isFalse();
    }
}
