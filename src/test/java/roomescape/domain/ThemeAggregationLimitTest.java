package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.ThemeAggregationLimit;

class ThemeAggregationLimitTest {

    @Test
    @DisplayName("집계 수를 가져온다.")
    void getAggregationLimit() {
        // when
        final int limit = ThemeAggregationLimit.getAggregationLimit();

        // then
        assertThat(limit).isEqualTo(10);
    }
}
