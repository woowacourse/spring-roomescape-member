package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.exceptions.InvalidInputException;

public class EmailTest {

    @ParameterizedTest
    @DisplayName("이메일에 빈 값이 아닌 값이 입력되었는지 확인한다.")
    @ValueSource(strings = {"", " "})
    void checkEmailBlank(String email) {
        //given & when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이메일이 입력되지 않았습니다. \"user@example.com\" 의 형식으로 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("이메일이 형식에 맞게 입력되었는지 확인한다.")
    @ValueSource(strings = {"aaaaaaa", "@example.com", "user@example,com", "user@@example.com"})
    void checkEmailFormat(String email) {
        //given & when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage(email + "은 유효하지 않은 이메일입니다. \"user@example.com\" 의 형식으로 입력해주세요.");
    }
}
