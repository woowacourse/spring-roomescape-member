package roomescape.unit.domain.theme;

import static org.assertj.core.api.Assertions.*;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.LastWeekRange;

public class LastWeekRangeTest {

    @Test
    void clock은_null일_수_없다() {
        assertThatThrownBy(() -> new LastWeekRange(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 시작일은_현재_기준_7일_전이다() {
        // given
        LocalDate beforeSevenDays = LocalDate.now(FIXED_CLOCK);
        LastWeekRange lastWeekRange = new LastWeekRange(FIXED_CLOCK);

        // when
        LocalDate start = lastWeekRange.getStartDate();

        // then
        assertThat(start).isEqualTo(beforeSevenDays.minusDays(7));
    }

    @Test
    void 종료일은_현재_기준_하루_전이다() {
        // given
        LocalDate beforeSevenDays = LocalDate.now(FIXED_CLOCK);
        LastWeekRange lastWeekRange = new LastWeekRange(FIXED_CLOCK);

        // when
        LocalDate end = lastWeekRange.getEndDate();

        // then
        assertThat(end).isEqualTo(beforeSevenDays.minusDays(1));
    }
}
