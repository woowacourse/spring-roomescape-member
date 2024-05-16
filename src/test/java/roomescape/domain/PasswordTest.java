package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.member.Password;
import roomescape.exception.EmptyParameterException;
import roomescape.exception.ParameterException;

class PasswordTest {
    @DisplayName("비밀번호는 5자 이상, 20자 이하가 아닐 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234", "123456789123456789123456789"})
    void passwordLengthTest(String password) {
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(ParameterException.class)
                .hasMessage("비밀번호는 5자 이상, 20자 이하여야 합니다.");
    }

    @DisplayName("비밀번호는 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
    @ParameterizedTest
    void validateNonBlankPassword(String password) {
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(EmptyParameterException.class)
                .hasMessage("비밀번호가 비어 있습니다.");
    }
}
