package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.reservation.ReservationInPastException;

class ReservationDateTimeTest {

    @DisplayName("예약은 이미 지난 시간으로 할 수 없다")
    @Test
    void reservationInPastTest() {
        // given
        LocalDate date = LocalDate.now();
        ReservationTime time = ReservationTime.createWithoutId(LocalTime.now().minusMinutes(1));

        // when & then
        assertThatThrownBy(() -> new ReservationDateTime(date, time))
                .isInstanceOf(ReservationInPastException.class);
    }

}