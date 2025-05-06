package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @DisplayName("식별자인 id가 동일하면 같은 테마로 취급한다.")
    @Test
    void sameReservation_whenSameId() {
        // given
        Theme theme1 = Theme.of(1L, "테마", "설명", "썸네일");
        Theme theme2 = Theme.of(1L, "테마2", "설명2", "썸네일2");

        // when & then
        assertThat(theme1).isEqualTo(theme2);
    }

    @DisplayName("식별자가 null일 때 비교 시 항상 다른 테마와 동일취급되지 않는다.")
    @Test
    void noSameReservation_whenNullId() {
        // given
        Theme theme1 = Theme.of(null, "테마", "설명", "썸네일");
        Theme theme2 = Theme.of(null, "테마", "설명", "썸네일");

        // when & then
        assertThat(theme1).isNotEqualTo(theme2);
    }
}
