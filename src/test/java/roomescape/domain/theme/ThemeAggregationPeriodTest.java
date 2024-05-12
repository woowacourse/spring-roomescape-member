package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.DATE_MAY_EIGHTH;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.ThemeAggregationPeriod;

import java.time.LocalDate;

class ThemeAggregationPeriodTest {

    @Test
    @DisplayName("집계 기간을 계산한다.")
    void calculateAggregationPeriod() {
        // given
        final LocalDate date = LocalDate.parse("2034-05-08");

        // when
        final LocalDate aggregationPeriod = ThemeAggregationPeriod.calculateAggregationPeriod(date);

        // then
        assertThat(aggregationPeriod).isEqualTo(LocalDate.parse("2034-05-01"));
    }
}
