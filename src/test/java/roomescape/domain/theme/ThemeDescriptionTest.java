package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeDescriptionTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("테마 설명은 비어있거나 공백이명 예외가 발생한다.")
    void createThemeDescriptionWhenNullOrBlank(String given) {
        //when //then
        assertThatThrownBy(() -> ThemeDescription.from(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 비어있을 수 없습니다.");
    }
}
