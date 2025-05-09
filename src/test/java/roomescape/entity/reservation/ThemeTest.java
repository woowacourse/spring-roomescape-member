package roomescape.entity.reservation;

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
    void validationName(String name) {
        // given
        String description = "test";
        String thumbnail = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마명입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("테마 설명이 null이나 공백일 경우, 예외가 발생한다.")
    void validationDescription(String description) {
        // given
        String name = "test";
        String thumbnail = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마 설명입니다.");
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("썸네일 주소가 null이나 공백일 경우, 예외가 발생한다.")
    void validationThumbnail(String thumbnail) {
        // given
        String description = "test";
        String name = "test";

        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 테마 썸네일 주소입니다.");
    }
}
