package roomescape.time.domain;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {
    @Test
    void 식별자가_같은_경우_동등한_객체로_판단한다() {
        // given
        ReservationTime reservationTimeA = new ReservationTime(1L, LocalTime.of(10,1));
        ReservationTime reservationTimeB = new ReservationTime(1L, LocalTime.of(10,2));

        // when & then
        Assertions.assertThat(reservationTimeA).isEqualTo(reservationTimeB);
    }
}
