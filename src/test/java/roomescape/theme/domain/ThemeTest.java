package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    @DisplayName("Theme 객체를 정상적으로 생성할 수 있다.")
    void test1() {
        // given
        Long id = 1L;
        String name = "이름";
        String description = "설명";
        String thumbnail = "썸네일";

        // when
        Theme theme = new Theme(id, name, description, thumbnail);

        // then
        assertThat(theme.id()).isEqualTo(id);
        assertThat(theme.name()).isEqualTo(name);
        assertThat(theme.description()).isEqualTo(description);
        assertThat(theme.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("DB에 저장하기 이전 기본 id로 Theme 객체를 생성할 수 있다.")
    void test2() {
        // given
        Long notSavedDefaultId = 0L;
        String name = "이름";
        String description = "설명";
        String thumbnail = "썸네일";

        // when
        Theme theme = Theme.createBeforeSaved(name, description, thumbnail);

        // then
        assertThat(theme.id()).isEqualTo(notSavedDefaultId);
        assertThat(theme.name()).isEqualTo(name);
        assertThat(theme.description()).isEqualTo(description);
        assertThat(theme.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("id가 null인 경우 예외가 발생한다.")
    void test3() {
        // given
        String name = "이름";
        String description = "설명";
        String thumbnail = "썸네일";

        // when & then
        assertThatThrownBy(() -> new Theme(null, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id가 null이어서는 안 됩니다.");
    }

    @Test
    @DisplayName("이름이 null이거나 빈 값인 경우 예외가 발생한다.")
    void test4() {
        // given
        Long id = 1L;
        String description = "설명";
        String thumbnail = "썸네일";

        // when & then
        assertThatThrownBy(() -> new Theme(id, null, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름이 null이거나 빈 값이어서는 안 됩니다.");

        assertThatThrownBy(() -> new Theme(id, "   ", description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름이 null이거나 빈 값이어서는 안 됩니다.");
    }

    @Test
    @DisplayName("설명이 null이거나 빈 값인 경우 예외가 발생한다.")
    void test5() {
        // given
        Long id = 1L;
        String name = "이름";
        String thumbnail = "썸네일";

        // when & then
        assertThatThrownBy(() -> new Theme(id, name, null, thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 설명이 null이거나 빈 값이어서는 안 됩니다.");

        assertThatThrownBy(() -> new Theme(id, name, " ", thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 설명이 null이거나 빈 값이어서는 안 됩니다.");
    }

    @Test
    @DisplayName("썸네일이 null이거나 빈 값인 경우 예외가 발생한다.")
    void test6() {
        // given
        Long id = 1L;
        String name = "이름";
        String description = "설명";

        // when & then
        assertThatThrownBy(() -> new Theme(id, name, description, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 썸네일이 null이거나 빈 값이어서는 안 됩니다.");

        assertThatThrownBy(() -> new Theme(id, name, description, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 썸네일이 null이거나 빈 값이어서는 안 됩니다.");
    }
}
