package roomescape.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.impl.ThemeNameMaxLengthExceedException;

public class ThemeTest {

    @Test
    @DisplayName("테마 이름은 20자를 넘길 수 없다.")
    void themeNameDoseNotOver20() {
        //given
        String wrongName = "12345678910123456768910";
        String description = "설명";
        String thumbnail = "썸네일";
        //when
        //then
        Assertions.assertThatThrownBy(() -> Theme.beforeSave(wrongName, description, thumbnail))
                .isInstanceOf(ThemeNameMaxLengthExceedException.class);
    }
}
