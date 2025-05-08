package roomescape.theme.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("이름이 null이나 공백일 경우, 예외가 발생한다.")
    void error_validationName(String name) {
        // given
        var description = "test";
        var thumbnail = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마명입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("테마 설명이 null이나 공백일 경우, 예외가 발생한다.")
    void error_validationDescription(String description) {
        // given
        var name = "test";
        var thumbnail = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마 설명입니다.");
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("썸네일 주소가 null이나 공백일 경우, 예외가 발생한다.")
    void error_validationThumbnail(String thumbnail) {
        // given
        var description = "test";
        var name = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마 썸네일 주소입니다.");
    }
}
