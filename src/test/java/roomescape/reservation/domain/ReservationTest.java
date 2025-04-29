package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationTime.domain.ReservationTime;

class ReservationTest {

    @DisplayName("지나간 날짜에 대한 예약을 생성할 시 예외가 발생한다")
    @Test
    void test1() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        //when&then
        Assertions.assertThatThrownBy(() -> new Reservation(1L, "riwon", LocalDate.now().minusDays(1), reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 지난 날짜로는 예약할 수 없습니다.");
    }

    @DisplayName("지나간 시간에 대한 예약을 생성할 시 예외가 발생한다")
    @Test
    void test2() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now().minusMinutes(1));

        //when&then
        Assertions.assertThatThrownBy(() -> new Reservation(1L, "riwon", LocalDate.now(), reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 지난 시간으로는 예약할 수 없습니다.");
    }
}
