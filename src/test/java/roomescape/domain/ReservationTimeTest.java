package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("현재 시간보다 과거의 시간인지 확인한다.")
    @Test
    void isPast() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(0, 0));

        // when
        boolean time = reservationTime.isPast();

        // then
        assertThat(time).isTrue();
    }
}
