package roomescape.reservationTime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 현재_지난_시간이라면_true() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().minusHours(1));

        // when
        boolean result = reservationTime.isBefore(LocalTime.now());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 현재보다_미래_시간이라면_false() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));

        // when
        boolean result = reservationTime.isBefore(LocalTime.now());

        // then
        assertThat(result).isFalse();
    }

}