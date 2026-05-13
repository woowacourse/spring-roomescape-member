package roomescape.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 8, 10, 30);
    private static final ReservationTime PAST_TIME = ReservationTime.of(1L, LocalTime.of(10, 0), LocalTime.of(10, 30));
    private static final ReservationTime FUTURE_TIME = ReservationTime.of(2L, LocalTime.of(11, 0), LocalTime.of(11, 30));
    private static final Theme THEME = Theme.of(1L, "링", "공포 테마", "http:~");

    @Test
    void 현재_이전_시간으로_예약을_생성할_수_없다() {
        assertThatThrownBy(() -> Reservation.create(
                "브라운",
                LocalDate.of(2026, 5, 8),
                PAST_TIME,
                THEME,
                NOW
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 현재_이후_시간으로_예약을_생성한다() {
        Reservation reservation = Reservation.create(
                "브라운",
                LocalDate.of(2026, 5, 8),
                FUTURE_TIME,
                THEME,
                NOW
        );

        assertThat(reservation.getCustomerName()).isEqualTo("브라운");
    }

    @Test
    void 과거_예약도_복원할_수_있다() {
        Reservation reservation = Reservation.of(
                1L,
                "브라운",
                LocalDate.of(2026, 5, 8),
                PAST_TIME,
                THEME
        );

        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 8));
    }

    @Test
    void 과거_예약은_취소할_수_없다() {
        Reservation reservation = Reservation.of(
                1L,
                "브라운",
                LocalDate.of(2026, 5, 8),
                PAST_TIME,
                THEME
        );

        assertThat(reservation.canCancel(NOW)).isFalse();
    }

    @Test
    void 현재_이후_예약은_취소할_수_있다() {
        Reservation reservation = Reservation.of(
                1L,
                "브라운",
                LocalDate.of(2026, 5, 8),
                FUTURE_TIME,
                THEME
        );

        assertThat(reservation.canCancel(NOW)).isTrue();
    }
}
