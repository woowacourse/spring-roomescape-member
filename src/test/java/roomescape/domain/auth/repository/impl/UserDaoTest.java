package roomescape.domain.auth.repository.impl;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.utils.PasswordFixture;

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
        final User user = User.withoutId(new Name("test"), email, PasswordFixture.generate(), Roles.USER);

        userRepository.save(user);

        // when
        final User result = userRepository.findByEmail(email)
                .get();

        // then
        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(result.getEmail())
                .isEqualTo(email);
        softly.assertThat(result.getPassword())
                .isEqualTo(user.getPassword());
        softly.assertThat(result.getName())
                .isEqualTo(user.getName());
        softly.assertThat(result.getRole())
                .isEqualTo(user.getRole());

        softly.assertAll();
    }

}
