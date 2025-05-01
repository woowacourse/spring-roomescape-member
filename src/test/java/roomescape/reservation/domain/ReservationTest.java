package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        final String name = null;
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        final Theme theme = new Theme("헤일러", "헤일러 설명", "헤일러 썸네일");

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime, theme);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름이_빈_문자열이면_예외가_발생한다() {
        // given
        final String name = "";
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        final Theme theme = new Theme("헤일러", "헤일러 설명", "헤일러 썸네일");

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime, theme);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 날짜가_null이면_예외가_발생한다() {
        // given
        final String name = "피케이";
        final LocalDate date = null;
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        final Theme theme = new Theme("헤일러", "헤일러 설명", "헤일러 썸네일");

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime, theme);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        // given
        final String name = "강산";
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = null;
        final Theme theme = new Theme("헤일러", "헤일러 설명", "헤일러 썸네일");

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime, theme);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마가_null이면_예외가_발생한다() {
        // given
        final String name = "강산";
        final LocalDate date = LocalDate.of(2025, 4, 24);
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        final Theme theme = null;

        // when & then
        Assertions.assertThatThrownBy(() -> {
            new Reservation(name, date, reservationTime, theme);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
