package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.presentation.controller.ThemeRankingCondition;

class ThemeRankingConditionTest {

    @Test
    @DisplayName("모든 파라미터가 null일 경우, 기본값이 적용된다")
    void applyDefaultsWhenAllParamsNull() {
        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(null, null, null);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        assertAll(
                () -> assertThat(condition.startDate()).isEqualTo(today.minusDays(7)),
                () -> assertThat(condition.endDate()).isEqualTo(today.minusDays(1)),
                () -> assertThat(condition.limit()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("유효한 파라미터가 주어졌을 경우 그대로 사용된다")
    void useProvidedValues() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 7);
        int limit = 20;

        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(start, end, limit);

        assertAll(
                () -> assertThat(condition.startDate()).isEqualTo(start),
                () -> assertThat(condition.endDate()).isEqualTo(end),
                () -> assertThat(condition.limit()).isEqualTo(limit)
        );
    }

    @Test
    @DisplayName("limit이 최대값보다 큰 경우 MAX_LIMIT(100)으로 제한된다")
    void limitIsCappedAtMaximum() {
        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(null, null, 999);

        assertThat(condition.limit()).isEqualTo(100);
    }

    @Test
    @DisplayName("limit이 null이면 DEFAULT_LIMIT(10)이 적용된다")
    void limitIsDefaultedWhenNull() {
        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(null, null, null);

        assertThat(condition.limit()).isEqualTo(10);
    }

}
