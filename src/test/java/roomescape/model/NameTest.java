package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {

    @DisplayName("이름에 공백 입력")
    @ParameterizedTest
    @ValueSource(strings = {"     ", " ", ""})
    void blankName(final String value) {
        assertThatThrownBy(() -> new UserName(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름에 숫자만 입력")
    @ParameterizedTest
    @ValueSource(strings = {"123", "456"})
    void numericName(final String value) {
        assertThatThrownBy(() -> new UserName(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("올바른 이름 입력")
    @ParameterizedTest
    @ValueSource(strings = {"abc123", "감자", "Joo Woo"})
    void validName(final String value) {
        assertThatCode(() -> new UserName(value))
                .doesNotThrowAnyException();
    }
}
