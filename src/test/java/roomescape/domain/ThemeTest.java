package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    @DisplayName("테마명에 null 값이 입력되면 에러가 발생한다.")
    void checkNullThemeName() {
        //given & when & then
        assertThatThrownBy(() -> new Theme(null, "설명", "이미지"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마명이 입력되지 않았습니다.");
    }

    @Test
    @DisplayName("테마명에 빈 값이 입력되면 에러가 발생한다.")
    void checkBlankThemeName() {
        //given & when & then
        assertThatThrownBy(() -> new Theme("", "설명", "이미지"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마명이 입력되지 않았습니다.");
    }

    @Test
    @DisplayName("테마 설명에 null 값이 입력되면 에러가 발생한다.")
    void checkNullThemeDescription() {
        //given & when & then
        assertThatThrownBy(() -> new Theme("이름", null, "이미지"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명이 입력되지 않았습니다.");
    }

    @Test
    @DisplayName("테마 설명에 빈 값이 입력되면 에러가 발생한다.")
    void checkBlankThemeDescription() {
        //given & when & then
        assertThatThrownBy(() -> new Theme("이름", "", "이미지"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명이 입력되지 않았습니다.");
    }

    @Test
    @DisplayName("테마 이미지에 null 값이 입력되면 에러가 발생한다.")
    void checkNullThemeThumbnail() {
        //given & when & then
        assertThatThrownBy(() -> new Theme("이름", "설명", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이미지가 입력되지 않았습니다.");
    }

    @Test
    @DisplayName("테마 이미지에 빈 값이 입력되면 에러가 발생한다.")
    void checkBlankThemeThumbnail() {
        //given & when & then
        assertThatThrownBy(() -> new Theme("이름", "설명", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이미지가 입력되지 않았습니다.");
    }

}
