package roomescape.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @DisplayName("비밀번호를 생성한다")
    @ValueSource(strings = {"123456", "12345678901234567890", "password1234", "password!@#"})
    @ParameterizedTest
    void when_createPassword_then_created(String password) {
        // when, then
        assertThatCode(() -> new Password(password))
                .doesNotThrowAnyException();
    }

    @DisplayName("비밀번호가 잘못된 형식으로 생성하면 예외가 발생한다")
    @ValueSource(strings = {"", "12345", "%$^#$#@()^%#", "123456789012345678901"})
    @ParameterizedTest
    void when_createPasswordWithInvalidFormat_then_throwException(String password) {
        // when, then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
