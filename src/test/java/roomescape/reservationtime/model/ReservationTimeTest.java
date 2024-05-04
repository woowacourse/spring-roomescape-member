package roomescape.reservationtime.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간 생성 시 시작 시간이 빈 값인 경우 예외가 발생한다.")
    void createReservationTime() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간 생성 시 시작 시간은 필수입니다.");
    }

    @Nested
    class isBefore {
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
}
