package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("모든 필드 값이 동일하면 true를 반환한다")
    void isEqualValueTrue() {
        LocalDate date = LocalDate.now();
        Reservation reservation = new Reservation(1L, "브라운", date, 1L, 1L);
        ReservationCommand command = new ReservationCommand("브라운", date.toString(), 1L, 1L);

        assertThat(reservation.isEqualValue(command)).isTrue();
    }

    @Test
    @DisplayName("필드 중 하나라도 값이 다르면 false를 반환한다")
    void isEqualValueFalse() {
        LocalDate date = LocalDate.now();
        Reservation reservation = new Reservation(1L, "브라운", date, 1L, 1L);

        ReservationCommand differentName = new ReservationCommand("조이", date.toString(), 1L, 1L);
        ReservationCommand differentDate = new ReservationCommand("브라운", date.plusDays(1).toString(), 1L, 1L);

        assertThat(reservation.isEqualValue(differentName)).isFalse();
        assertThat(reservation.isEqualValue(differentDate)).isFalse();
    }
}