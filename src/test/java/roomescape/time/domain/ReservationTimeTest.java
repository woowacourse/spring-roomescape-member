package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("식별자인 id가 동일하면 같은 시간으로 취급한다.")
    @Test
    void sameReservation_whenSameId() {
        // given
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(1L, LocalTime.of(11, 0));

        // when & then
        assertThat(time1).isEqualTo(time2);
    }

    @DisplayName("식별자가 null일 때 비교 시 항상 다른 시간과 동일취급되지 않는다.")
    @Test
    void noSameReservation_whenNullId() {
        // given
        ReservationTime time1 = ReservationTime.of(null, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(null, LocalTime.of(10, 0));

        // when & then
        assertThat(time1).isNotEqualTo(time2);
    }
}
