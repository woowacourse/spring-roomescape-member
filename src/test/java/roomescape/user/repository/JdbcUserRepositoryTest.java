package roomescape.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.user.domain.dto.User;

@JdbcTest
@Import({JdbcUserRepository.class})
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    @DisplayName("이메일과 비밀번호가 일치하는 User를 찾을 수 있다.")
    @Test
    void findUserByEmailAndPassword_byEmailAndPassword() {
        // given
        User user = new User(1L, "name", "email", "password");
        User expected = jdbcUserRepository.save(user);

        // when
        User actual = jdbcUserRepository.findUserByEmailAndPassword(user.getEmail(),
                user.getPassword());

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("이메일과 비밀번호가 일치하는 User가 존재하는 지를 검사할 수 있다.")
    @Test
    void existUserByEmailAndPassword() {
        // given
        User user = new User(1L, "name", "email", "password");
        jdbcUserRepository.save(user);

        // when
        boolean actual = jdbcUserRepository.existUserByEmailAndPassword(user.getEmail(),
                user.getPassword());

        // then
        Assertions.assertThat(actual).isTrue();
    }
}
