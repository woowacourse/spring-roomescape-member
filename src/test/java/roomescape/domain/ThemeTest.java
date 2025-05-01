package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ThemeTest {

    @DisplayName("비어있는 ID값으로 테마를 생성할 수 없다")
    @Test
    void cannotCreateBecauseNullId() {
        Long nullId = null;
        assertThatThrownBy(() -> new Theme(nullId, "이름", "설명", "썸네일"));
    }

    @DisplayName("비어있는 이름으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullName(String name) {
        assertThatThrownBy(() -> new Theme(1L, name, "설명", "썸네일"));
    }

    @DisplayName("비어있는 이름으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullDescription(String description) {
        assertThatThrownBy(() -> new Theme(1L, "이름", description, "썸네일"));
    }

    @DisplayName("비어있는 이름으로 테마를 생성할 수 없다")
    @ParameterizedTest
    @NullAndEmptySource
    void cannotCreateBecauseNullThumbnail(String thumbnail) {
        assertThatThrownBy(() -> new Theme(1L, "이름", "설명", thumbnail));
    }
}
