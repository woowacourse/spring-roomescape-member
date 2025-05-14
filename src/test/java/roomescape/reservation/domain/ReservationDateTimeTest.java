package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.exception.ReservationInPastException;
import roomescape.reservationtime.domain.ReservationTime;

class ReservationDateTimeTest {

    @DisplayName("예약은 이미 지난 시간으로 할 수 없다")
    @Test
    void reservationInPastTest() {
        // given
        ReservationDate date = new ReservationDate(LocalDate.now());
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.now().minusMinutes(1));

        // when & then
        assertThatThrownBy(() -> new ReservationDateTime(date, time))
                .isInstanceOf(ReservationInPastException.class);
    }

}