package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 유효한_값으로_예약을_생성하면_필드가_저장된다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), time);

        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @Test
    void 예약자_이름이_빈_문자열이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, "", LocalDate.of(2026, 5, 1), time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약자_이름이_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, null, LocalDate.of(2026, 5, 1), time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_날짜가_null이면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        assertThatThrownBy(() -> new Reservation(1L, "브라운", null, time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Reservation(1L, "브라운", LocalDate.of(2026, 5, 1), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
