package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeNameTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new ThemeName("방탈출 레벨 1"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "\r", "\n"})
    @DisplayName("제목은 공백을 제외한 1글자 이상이다.")
    void validateBlank(String source) {
        assertThatThrownBy(() -> new ThemeName(source))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 공백을 제외한 1글자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("제목은 100글자를 넘을 수 없다.")
    void maxLength() {
        String longTitle = "*".repeat(101);

        assertThatThrownBy(() -> new ThemeName(longTitle))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 100글자를 넘을 수 없습니다.");
    }
}
