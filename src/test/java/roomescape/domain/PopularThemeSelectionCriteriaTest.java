package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PopularThemeSelectionCriteriaTest {

    @DisplayName("인기 테마 선택 기준을 생성한다")
    @Test
    void createPopularThemeSelectionCriteriaTest() {
        // given
        final LocalDate startBaseDate = LocalDate.of(2025, 5, 5);
        final int durationInDays = 10;

        // when
        PopularThemeSelectionCriteria criteria = new PopularThemeSelectionCriteria(startBaseDate, durationInDays);

        // then
        assertThat(criteria.getStartDay()).isEqualTo(LocalDate.of(2025, 4, 24));
        assertThat(criteria.getEndDay()).isEqualTo(LocalDate.of(2025, 5, 4));
    }
}
