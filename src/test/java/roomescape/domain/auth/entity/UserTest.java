package roomescape.domain.auth.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.infrastructure.security.Sha256PasswordEncryptor;

class UserTest {

    @BeforeEach
    void init() {
        Mockito.reset();
    }

    @DisplayName("이메일이 형식에 맞지 않다면 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {"@naver.com", "tdasdsad@", "sdadsa@naver"})
    void emailTest(final String email) {
        // given
        final Name name = new Name("꾹");
        final Password password = mock(Password.class);

        // when
        // then
        assertThatThrownBy(() -> {
            new User(1L, name, email, password, Roles.USER);
        }).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("유저 생성 성공 테스트")
    @Test
    void userTest1() {
        // given
        final String email = "tizm@naver.com";
        final Name name = mock(Name.class);
        final Password password = mock(Password.class);

        // when
        // then
        assertThatCode(() -> {
            new User(1L, name, email, password, Roles.USER);
        }).doesNotThrowAnyException();
    }

    @DisplayName("비밀번호가 다르다면 예외를 반환한다")
    @Test
    void login_throwsException() {
        // given
        final String email = "sdaas@naver.com";
        final PasswordEncryptor passwordEncryptor = new Sha256PasswordEncryptor();
        final Password password = Password.encrypt("1234", passwordEncryptor);
        final User user = new User(1L, new Name("꾹"), email, password, Roles.USER);

        // when & then
        assertThatThrownBy(() -> {
            user.login(email, "123", passwordEncryptor);
        }).isInstanceOf(InvalidAuthorizationException.class);
    }
}
