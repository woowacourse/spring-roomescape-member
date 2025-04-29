package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        final String name = null;
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_빈_문자열이면_예외가_발생한다() {
        // given
        final String name = "";
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예외가_발생한다() {
        // given
        final String name = "피케이";
        final LocalDate date = null;
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        // given
        final String name = "강산";
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = null;

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
