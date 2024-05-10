package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AggregationLimitTest {

    @Test
    @DisplayName("집계 수를 가져온다.")
    void getAggregationLimit() {
        // when
        final int limit = AggregationLimit.getAggregationLimit();

        // then
        assertThat(limit).isEqualTo(10);
    }
}
