package roomescape.business.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("예약 시간이 과거인지 확인한다.")
    @Test
    void isInThePastTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = now.minusDays(1);
        ReservationTime reservationTime = new ReservationTime(reservationDateTime.toLocalTime());

        // when
        boolean isInThePast = reservationTime.isInThePast(reservationDateTime.toLocalDate());

        // then
        assertThat(isInThePast)
                .isTrue();
    }

    @DisplayName("예약 시간이 미래인지 확인한다.")
    @Test
    void isInTheFutureTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = now.plusDays(1);
        ReservationTime reservationTime = new ReservationTime(reservationDateTime.toLocalTime());

        // when
        boolean isInThePast = reservationTime.isInThePast(reservationDateTime.toLocalDate());

        // then
        assertThat(isInThePast)
                .isFalse();
    }
}
