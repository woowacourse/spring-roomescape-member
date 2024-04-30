package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    @DisplayName("동일한 id 가 있다면 true를 반환한다")
    void isSameIdTest() {
        Reservation reservation = new Reservation(1L, "hogi", LocalDate.now(),
                new ReservationTime(1L, LocalTime.now()));
        assertAll(
                () -> assertThat(reservation.isSameId(1L)).isTrue(),
                () -> assertThat(reservation.isSameId(2L)).isFalse()
        );
    }
}
