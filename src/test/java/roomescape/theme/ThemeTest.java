package roomescape.theme;

import org.assertj.core.api.Assertions;
import roomescape.exception.ArgumentNullException;

import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    void 이름이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> new Theme(1L, null, "description", "thumb"))
                .isInstanceOf(ArgumentNullException.class);
    }

    @Test
    void 설명이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> new Theme(1L, "name", null, "thumb"))
                .isInstanceOf(ArgumentNullException.class);
    }

    @Test
    void 설명이_5글자_미만이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "hi", "thumb"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 설명이_100글자_초과이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you ", "thumb"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 썸네일이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> new Theme(1L, "name", "description", null))
                .isInstanceOf(ArgumentNullException.class);
    }
}
