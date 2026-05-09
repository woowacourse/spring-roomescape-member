package roomescape.reservation.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

class ReservationTest {

    @Test
    void 예약자_이름이_비어있으면_검증에_실패한다() {
        assertThatThrownBy(() -> Reservation.of("", LocalDate.of(2026, 5, 9), reservationTime(), createTheme()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약자_이름이_10자를_초과하면_검증에_실패한다() {
        assertThatThrownBy(
                () -> Reservation.of("가나다라마바사아자차카", LocalDate.of(2026, 5, 9), reservationTime(), createTheme()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_날짜가_null이면_검증에_실패한다() {
        assertThatThrownBy(() -> Reservation.of("봉구스", null, reservationTime(), createTheme()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_시간이_null이면_검증에_실패한다() {
        assertThatThrownBy(() -> Reservation.of("봉구스", LocalDate.of(2026, 5, 9), null, createTheme()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_테마가_null이면_검증에_실패한다() {
        assertThatThrownBy(() -> Reservation.of("봉구스", LocalDate.of(2026, 5, 9), reservationTime(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private ReservationTime reservationTime() {
        return ReservationTime.of(LocalTime.of(10, 0));
    }

    private Theme createTheme() {
        return Theme.of("테마", "설명", "https://example.com/theme.png", Duration.ofHours(1));
    }
}
