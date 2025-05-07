package roomescape.domain.auth.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;

@JdbcTest
@Import(UserDao.class)
class UserDaoTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일을 통해 유저를 찾을 수 있다.")
    @Test
    void findByEmailTest1() {
        // given
        final String email = "test@example.com";
        final User user = User.withoutId(new Name("test"), email, "password");

        userRepository.save(user);

        // when

        final User result = userRepository.findByEmail(email)
                .get();

        // then
        assertThat(result.getEmail()).isEqualTo(email);
    }

}
