package roomescape.user.repository;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.user.MemberTestDataConfig;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@JdbcTest
@Import({JdbcUserRepository.class, MemberTestDataConfig.class})
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    @Nested
    @DisplayName("저장된 모든 유저 불러오는 기능")
    class findAll {

        @DisplayName("데이터가 있을 때 모든 유저를 불러온다")
        @Test
        void findAll_success_whenDataExists() {
            // given
            // when
            List<User> users = jdbcUserRepository.findAll();

            // then
            Assertions.assertThat(users).hasSize(1);
        }

        @DisplayName("데이터가 없더라도 예외 없이 빈 리스트를 반환한다")
        @Test
        void findAll_success_whenNoData() {
            // given
            deleteAll();

            // when
            List<User> users = jdbcUserRepository.findAll();

            // then
            Assertions.assertThat(users).hasSize(0);
        }

        private void deleteAll() {
            jdbcTemplate.update("delete from users");
        }
    }

    @DisplayName("이메일과 비밀번호가 일치하는 User를 찾을 수 있다.")
    @Test
    void findUserByEmailAndPassword_byEmailAndPassword() {
        // given
        User user = new User(1L, Role.ROLE_MEMBER, "name", "email", "password");
        User expected = jdbcUserRepository.save(user);

        // when
        User actual = jdbcUserRepository.findUserByEmailAndPassword(user.getEmail(),
                        user.getPassword())
                .get();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("이메일과 비밀번호가 일치하는 User가 존재하는 지를 검사할 수 있다.")
    @Test
    void existUserByEmailAndPassword() {
        // given
        User user = new User(1L, Role.ROLE_MEMBER, "name", "email", "password");
        jdbcUserRepository.save(user);

        // when
        boolean actual = jdbcUserRepository.existUserByEmailAndPassword(user.getEmail(),
                user.getPassword());

        // then
        Assertions.assertThat(actual).isTrue();
    }
}
