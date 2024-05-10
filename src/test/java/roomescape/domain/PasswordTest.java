package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;

class PasswordTest {

    @ParameterizedTest
    @DisplayName("비밀번호에 빈 값이 아닌 값이 입력되었는지 확인한다.")
    @ValueSource(strings = {"", " "})
    void checkPasswordBlank(String password) {
        //given & when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("비밀번호가 입력되지 않았습니다. 6자 이상 20자 이하로 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("비밀번호가 6자 이상 20자 이하로 입력되었는지 확인한다.")
    @ValueSource(strings = {"1q2w!", "1q2w3e4r5t6y7u8i9o0p!@#$"})
    void checkPasswordLength(String password) {
        //given & when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(password + "은 유효하지 않은 비밀번호입니다. 6자 이상 20자 이하로 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("비밀번호가 영문, 숫자, 특수문자만을 모두 포함하여 입력되었는지 확인한다.")
    @ValueSource(strings = {"1q2w3e", "12345678", "abcdefgh123", "asdf!!@@"})
    void checkPasswordFormat(String password) {
        //given & when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(password + "은 유효하지 않은 비밀번호입니다. 영문과 숫자, 특수문자만을 모두 포함해야 합니다.");
    }
}
