package roomescape.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 예약_시간이_같은지_판단() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(null, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(null, LocalTime.of(10, 0));
        ReservationTime reservationTime3 = new ReservationTime(null, LocalTime.of(12, 0));

        //when, then
        assertAll(
                () -> assertThat(reservationTime1.isSameTime(reservationTime2)).isTrue(),
                () -> assertThat(reservationTime1.isSameTime(reservationTime3)).isFalse()
        );
    }
}