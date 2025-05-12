package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Theme;

public class ThemeTest {
    @Test
    void validate() {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Theme(1L, null, "테마 설명", "테마 이미지")
        );
    }

    @Test
    void validateBlankName() {
        // given
        String name = "  ";

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Theme(1L, name, "테마 설명", "테마 이미지")
        );
    }

    @Test
    void generateWithPrimaryKey() {
        // given
        Theme theme = new Theme(1L, "테마", "테마 설명", "테마 이미지");
        Long newPrimaryKey = 2L;

        // when
        Theme generatedTheme = Theme.generateWithPrimaryKey(theme, newPrimaryKey);

        // then
        assertThat(generatedTheme.getId()).isEqualTo(newPrimaryKey);
        assertThat(generatedTheme.getName()).isEqualTo(theme.getName());
        assertThat(generatedTheme.getDescription()).isEqualTo(theme.getDescription());
        assertThat(generatedTheme.getThumbnail()).isEqualTo(theme.getThumbnail());
    }
}
