package roomescape.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.domain.auth.dto.UserCreateRequest;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.repository.impl.UserDao;
import roomescape.infrastructure.security.Sha256PasswordEncryptor;
import roomescape.utils.PasswordFixture;

@JdbcTest
@Import({UserDao.class, UserService.class, Sha256PasswordEncryptor.class})
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    @DisplayName("어드민을 제외한 유저의 정보를 모두 가져온다.")
    @Test
    void getAllTest1() {
        // given
        final Password password = PasswordFixture.generate();
        final User user1 = User.withoutId(new Name("꾹"), "t1@naver.com", password, Roles.USER);
        final User user2 = User.withoutId(new Name("꾹"), "t2@naver.com", password, Roles.USER);
        final User user3 = User.withoutId(new Name("꾹"), "t3@naver.com", password, Roles.USER);
        final User user4 = User.withoutId(new Name("꾹"), "t4@naver.com", password, Roles.ADMIN);

        final List<User> users = List.of(user1, user2, user3, user4);

        for (final User user : users) {
            userRepository.save(user);
        }

        // when
        final List<UserInfoResponse> result = userService.getAll();

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("유저를 등록한다.")
    @Test
    void registerTest1() {
        // given
        final String name = "꾹";
        final String email = "t1@naver.com";
        final UserCreateRequest request = new UserCreateRequest(email, "1234", name);

        // when
        final UserInfoResponse result = userService.register(request);

        // then
        assertThat(result.name()).isEqualTo(name);
    }

    @DisplayName("이미 존재하는 이메일이면 AlreadyInUseException 예외를 반환한다")
    @Test
    void register_throwsException() {
        // given
        final String email = "t1@naver.com";
        final String rawPassword = "1234";
        final Password password = Password.encrypt(rawPassword, passwordEncryptor);
        userRepository.save(User.withoutId(new Name("꾹"), email, password, Roles.USER));

        final UserCreateRequest request = new UserCreateRequest(email, rawPassword, "sdad");

        // when & then
        assertThatThrownBy(() -> {
            userService.register(request);
        }).isInstanceOf(AlreadyInUseException.class);
    }

}
