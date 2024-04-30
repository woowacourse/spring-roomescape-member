package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class ThemeNameTest {
    @DisplayName("이름이 빈칸이나 공백이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    void invalidNameTest(String name) {
        assertThatThrownBy(() -> new ThemeName(name))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이름은 빈칸(공백)일 수 없습니다.");
    }
}
