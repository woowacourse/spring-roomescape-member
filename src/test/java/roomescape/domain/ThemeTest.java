package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.theme.InvalidThemeException;

class ThemeTest {

    @Test
    @DisplayName("테마를 정상적으로 생성한다.")
    void createTheme() {
        Theme theme = Theme.createNew("비밀 연구소", "바이러스를 막아라", "https://image.url/lab.png");

        assertThat(theme.getName()).isEqualTo("비밀 연구소");
        assertThat(theme.getId()).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("테마 이름, 설명, URL이 null이거나 공백이면 예외가 발생한다.")
    void validate_NullOrBlank(String invalidInput) {
        assertThatThrownBy(() -> Theme.createNew(invalidInput, "설명", "url"))
                .isInstanceOf(InvalidThemeException.class)
                .hasMessageContaining("비어있을 수 없습니다");

        assertThatThrownBy(() -> Theme.createNew("이름", invalidInput, "url"))
                .isInstanceOf(InvalidThemeException.class)
                .hasMessageContaining("비어있을 수 없습니다");
    }

    @Test
    @DisplayName("테마 이름이 255자를 초과하면 예외가 발생한다. (경계값 테스트)")
    void validateName_MaxLength() {
        String exactBoundary = "a".repeat(255);
        String overBoundary = "a".repeat(256);

        assertThatCode(() -> Theme.createNew(exactBoundary, "설명", "url"))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> Theme.createNew(overBoundary, "설명", "url"))
                .isInstanceOf(InvalidThemeException.class)
                .hasMessageContaining("255자를 초과할 수 없습니다");
    }
}
