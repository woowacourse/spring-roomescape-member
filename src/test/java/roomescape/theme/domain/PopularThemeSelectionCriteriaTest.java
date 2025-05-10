package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

class PopularThemeSelectionCriteriaTest {

    @DisplayName("인기 테마 선택 기준을 생성한다")
    @Test
    void createPopularThemeSelectionCriteriaTest() {
        // given
        final LocalDate startBaseDate = LocalDate.of(2025, 5, 5);
        final int durationInDays = 10;

        // when
        final PopularThemeSelectionCriteria criteria = new PopularThemeSelectionCriteria(startBaseDate, durationInDays);

        // then
        assertThat(criteria.getStartDay()).isEqualTo(LocalDate.of(2025, 4, 24));
        assertThat(criteria.getEndDay()).isEqualTo(LocalDate.of(2025, 5, 4));
    }

    @DisplayName("인기 테마 선택 기준을 생성 시, 시작 기준일이 null이면 예외를 던진다")
    @Test
    void createPopularThemeSelectionCriteriaTest_WhenStartBaseDateIsNull() {
        // given
        final int durationInDays = 10;

        // when // then
        assertThatCode(() -> new PopularThemeSelectionCriteria(null, durationInDays))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("시작 기준일은 null이 될 수 없습니다");
    }

    @DisplayName("인기 테마 선택 기준을 생성 시, 인기 테마 선정 기준 기한 일수가 음수나 0이면 예외를 던진다")
    @Test
    void createPopularThemeSelectionCriteriaTest_WhenDurationInDaysIsNegativeOrZero() {
        // given
        final LocalDate startBaseDate = LocalDate.of(2025, 5, 5);
        final int durationInDays = -1;

        // when // then
        assertThatCode(() -> new PopularThemeSelectionCriteria(startBaseDate, durationInDays))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("인기 테마 선정 기준 기한 일수는 음수나 0이 될 수 없습니다");
    }
}
