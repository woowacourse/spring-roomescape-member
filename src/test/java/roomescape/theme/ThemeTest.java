package roomescape.theme;

import org.assertj.core.api.Assertions;
import roomescape.exception.ArgumentException;

import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    void 이름이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId(null, "description", "thumb.jpg"))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 설명이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", null, "thumb.jpg"))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 설명이_5글자_미만이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "hi", "thumb.jpg"))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 설명이_100글자_초과이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you hi nice to meet you ", "thumb.jpg"))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 썸네일이_빈_값이면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "description", null))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 썸네일이_유효한_링크가_아니면_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> Theme.createWithoutId("name", "description", "thumb"))
                .isInstanceOf(ArgumentException.class);
    }
}
