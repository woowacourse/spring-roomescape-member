package roomescape.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.exception.ErrorCode;
import roomescape.model.Theme;

public class ThemeTest {

    @Test
    public void 테마_이름은_20자_이하를_가진다() {
        // given
        Theme theme = new Theme(1L, "붉은 요람", "아이의 울음소리가 멈추면, 비로소 당신의 비명이 시작됩니다.", "https://fake.html") {
        };

        // when
        int size = 5;
        int nameSize = theme.getName().length();

        // then
        assertEquals(size, nameSize);
    }

    @Test
    public void 테마_이름이_20자_초과_될_경우_예외가_발생한다() {
        // when & then
        String name = "dsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf";
        Assertions.assertThatThrownBy(() ->
                        new Theme(1L, name, "아이의 울음소리", "https://fake.com"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorCode.THEME_NAME_LENGTH_INVALID.getMessage());
    }

    @Test
    public void 테마_설명이_누락_될_경우_예외가_발생한다() {
        // when & then
        String name = "붉은 요람";
        Assertions.assertThatThrownBy(() ->
                        new Theme(1L, name, null, "https://fake.com"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorCode.THEME_DESCRIPTION_BLANK.getMessage());
    }

    @Test
    public void 테마_URL이_누락_될_경우_예외가_발생한다() {
        // when & then
        String name = "붉은 요람";
        Assertions.assertThatThrownBy(() ->
                        new Theme(1L, name, "아이의 울음소리", null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorCode.THEME_URL_BLANK.getMessage());
    }
}