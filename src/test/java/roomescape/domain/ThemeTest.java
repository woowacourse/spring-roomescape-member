package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ThemeTest {

    @DisplayName("비어있는 이름으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullName(String name) {
        // when & then
        assertThatThrownBy(() -> new Theme(1L, name, "설명", "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 이름으로 테마를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 설명으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullDescription(String description) {
        // when & then
        assertThatThrownBy(() -> new Theme(1L, "이름", description, "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 설명으로 테마를 생성할 수 없습니다.");
    }

    @DisplayName("비어있는 섬네일으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullThumbnail(String thumbnail) {
        // when & then
        assertThatThrownBy(() -> new Theme(1L, "이름", "설명", thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 썸네일으로 테마를 생성할 수 없습니다.");
    }
}
