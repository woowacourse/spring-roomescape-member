/*
package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {
    @DisplayName("시간이 같으면 true를 반환한다.")
    @Test
    void hasSameStartAt() {
        ReservationTime firstTime = new ReservationTime(1L, LocalTime.of(10,5));
        ReservationTime secondTime = new ReservationTime(2L, LocalTime.of(10,5));

        assertThat(firstTime.hasSameStartAt(secondTime)).isTrue();
    }

    @DisplayName("시간이 다르면 false를 반환한다.")
    @Test
    void hasNotSameStartAt() {
        ReservationTime firstTime = new ReservationTime(1L, LocalTime.of(10,5));
        ReservationTime secondTime = new ReservationTime(2L, LocalTime.of(11,5));

        assertThat(firstTime.hasSameStartAt(secondTime)).isFalse();
    }
}
*/
