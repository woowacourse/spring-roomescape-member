package roomescape.reservationtime.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("주어진 시간보다 예약 시간이 이전인 경우 참을 반환한다.")
    void isBefore() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(1, 2));
        assertTrue(reservationTime.isBefore(LocalTime.of(2, 1)));
    }

    @Test
    @DisplayName("주어진 시간보다 예약 시간이 이후인 경우 거짓을 반환한다.")
    void isBefore_WhenReservationTimeIsAfter() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(1, 2));
        assertFalse(reservationTime.isBefore(LocalTime.of(1, 1)));
    }

    @Test
    @DisplayName("주어진 시간보다 예약 시간이 같은 시간인 경우 거짓을 반환한다.")
    void isBefore_WhenReservationTimeIsSame() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(1, 2));
        assertFalse(reservationTime.isBefore(LocalTime.of(1, 2)));
    }
}
