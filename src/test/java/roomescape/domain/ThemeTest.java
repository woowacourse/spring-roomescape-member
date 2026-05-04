package roomescape.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.model.Theme;

public class ThemeTest {

    @Test
    public void 테마_이름은_1자_이상_20자_이하를_가진다(){
        // given
        Theme theme = new Theme(1L, "붉은 요람", "아이의 울음소리가 멈추면, 비로소 당신의 비명이 시작됩니다.", "https://fake.html") {};

        // when
        int size = 5;
        int nameSize = theme.getName().length();

        // then
        Assertions.assertEquals(size, nameSize);
    }

    @Test
    public void 테마_이름이_20자_초과_될_경우_예외가_발생한다(){
        // when & then
        String name = "dsdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf";
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Theme(1L, name, "아이의 울음소리", "https://fake.com"));
    }

}
