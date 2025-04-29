package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;

class ReservationTest {
    @Test
    void createReservation_shouldThrowException_whenTimeIsBeforeNow() {
        assertThatThrownBy(() -> Reservation.of(1L,
                "대니",
                LocalDate.now().minusDays(1),
                ReservationTime.of(1L, LocalTime.now()))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}