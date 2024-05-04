package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeThumbnailTest {
    @DisplayName("테마 설명이 null일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsNull() {
        assertThatThrownBy(() -> new ThemeThumbnail(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("테마 설명이 \"\"일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsEmpty() {
        assertThatThrownBy(() -> new ThemeThumbnail(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 썸네일은 한글자 이상이어야 합니다.");
    }
}
