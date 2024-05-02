package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeNameTest {

    @DisplayName("유효하지 않은 길이의 테마 이름이 입력되면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"테바와 코딩을 하고 있으니 제가 LA에 있을 시절이 떠오르는..."})
    void themeNameLengthTest(String invalidName) {
        // When & Then
        assertThatThrownBy(() -> new ThemeName(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 1글자 이상 10글자 이하여야 합니다.");
    }
}
