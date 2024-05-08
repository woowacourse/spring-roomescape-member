package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" "})
    @DisplayName("테마 이름에 공백 또는 빈 문자열이 입력되면 IllegalArgumentException 발생")
    void nameException(String name) {
        assertThatThrownBy(() -> new Theme(0, name, "description", "thumbnail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Theme name cannot be null or blank");
    }
}
