package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("예약 시간 도메인 테스트")
class ReservationTimeTest {
    @DisplayName("동일한 id는 같은 예약 시간이다.")
    @Test
    void equals() {
        //given
        long id1 = 1;
        LocalTime localTime1 = LocalTime.MIDNIGHT;
        LocalTime localTime2 = LocalTime.MIDNIGHT;

        //when
        ReservationTime reservationTime1 = new ReservationTime(id1, localTime1);
        ReservationTime reservationTime2 = new ReservationTime(id1, localTime2);

        //then
        assertThat(reservationTime1).isEqualTo(reservationTime2);
    }
}
