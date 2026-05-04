package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.ReservationTime;

class ReservationTest {

    @Test
    void id가_같으면_다른_값이어도_동일하다() {
        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(11, 0));

        Reservation first = new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time1);
        Reservation second = new Reservation(1L, "코니", LocalDate.of(2026, 5, 2), time2);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void id가_다르면_같은_값이어도_동일하지_않다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        Reservation first = new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time);
        Reservation second = new Reservation(2L, "브라운", LocalDate.of(2026, 5, 1), time);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void id가_null이면_동일하지_않다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        Reservation first = new Reservation(null, "브라운", LocalDate.of(2026, 5, 1), time);
        Reservation second = new Reservation(null, "브라운", LocalDate.of(2026, 5, 1), time);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }
}

