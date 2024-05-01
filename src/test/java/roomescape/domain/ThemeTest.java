package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThemeTest {

    @Test
    @DisplayName("성공 : id를 통해 동일한 테마인지 판별한다.")
    void checkSameTheme_Success() {
        Theme theme1 = new Theme(1L, "name1", "description1", "thumbnail1");
        Theme theme2 = new Theme(1L, "name2", "description2", "thumbnail2");

        assertThat(theme1.isSameTheme(theme2.getId())).isTrue();
    }

    @Test
    @DisplayName("실패 : id를 통해 동일한 테마인지 판별한다.")
    void checkSameReservationTime_Failure() {
        Theme theme1 = new Theme(1L, "name1", "description1", "thumbnail1");
        Theme theme2 = new Theme(2L, "name2", "description2", "thumbnail2");

        assertThat(theme1.isSameTheme(theme2.getId())).isFalse();
    }
}
