package roomescape.domain_entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("지난 예약 시간인 경우 TRUE 반환한다.")
    void isPastReservationTimeReturnsTrue() {
        // given
        LocalTime now = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(new Id(1L), now.minusMinutes(1));

        // when
        boolean result = reservationTime.isPastTime();

        // then
        assertThat(result).isTrue();
    }
}
