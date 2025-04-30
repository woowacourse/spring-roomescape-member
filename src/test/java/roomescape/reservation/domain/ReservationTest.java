package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.Time;

class ReservationTest {

    @DisplayName("지나간 날짜에 대한 예약을 생성할 시 예외가 발생한다")
    @Test
    void test1() {
        //given
        Time time = new Time(1L, LocalTime.now());

        //when&then
        Assertions.assertThatThrownBy(() -> new Reservation(
                1L, "riwon", LocalDate.now().minusDays(1), time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 지난 날짜로는 예약할 수 없습니다.");
    }

    @DisplayName("지나간 시간에 대한 예약을 생성할 시 예외가 발생한다")
    @Test
    void test2() {
        //given
        Time time = new Time(1L, LocalTime.now().minusMinutes(1));

        //when&then
        Assertions.assertThatThrownBy(() -> new Reservation(1L, "riwon", LocalDate.now(), time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 지난 시간으로는 예약할 수 없습니다.");
    }
}
