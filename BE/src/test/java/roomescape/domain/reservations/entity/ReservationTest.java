package roomescape.domain.reservations.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservations.entity.Reservation;
import roomescape.reservations.entity.ReservationTime;

public class ReservationTest {

    private ReservationTime createTime() {
        return ReservationTime.of(1L, LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("두 객체가 같은지 확인")
    void equalsTest() {
        // given
        Reservation res1 = Reservation.of(1L, "흑곰", LocalDate.of(2026, 3, 5), createTime());
        Reservation res2 = Reservation.of(1L, "흑곰", LocalDate.of(2026, 3, 5), createTime());
    }
}
