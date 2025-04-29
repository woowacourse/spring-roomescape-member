package roomescape.unit.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeTest {

    @Test
    void 예약시간은_NULL_일_수_없다() {
        Assertions.assertThatThrownBy(() -> new ReservationTime(1L, null)).isInstanceOf(IllegalArgumentException.class);
    }
}

