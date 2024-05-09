package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeTest {

    @Test
    @DisplayName("성공 : id를 통해 동일한 예약 시간인지 판별한다.")
    void checkSameReservationTime_Success() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThat(reservationTime1.isSameReservationTime(reservationTime2.getId())).isTrue();
    }

    @Test
    @DisplayName("실패 : id를 통해 동일한 예약 시간인지 판별한다.")
    void checkSameReservationTime_Failure() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(10, 0));

        assertThat(reservationTime1.isSameReservationTime(reservationTime2.getId())).isFalse();
    }
}
