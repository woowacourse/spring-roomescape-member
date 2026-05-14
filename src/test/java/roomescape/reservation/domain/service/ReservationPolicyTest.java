package roomescape.reservation.domain.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

class ReservationPolicyTest {

    private final ReservationPolicy policy = new ReservationPolicy();

    private Reservation reservation;
    private ReservationTime time;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .name("스타크")
                .date(LocalDate.of(2026, 5, 6))
                .themeId(1L)
                .timeId(1L)
                .build();
        time = ReservationTime.builder()
                .id(1L)
                .startAt(LocalTime.of(10, 0))
                .build();
    }

    @DisplayName("현재 시간보다 이전 예약 시간일 때 예외 발생을 테스트합니다.")
    @Test
    void validateReservable_past_throws_exception() {
        assertThatThrownBy(() -> policy.validateReservable(
                reservation, time, LocalDateTime.of(2026, 5, 6, 11, 0)
        ))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }

    @DisplayName("현재 시간과 예약 시간이 같을 때 예외가 발생하지 않음을 테스트합니다.")
    @Test
    void validateReservable_same_time_no_exception() {
        assertThatNoException().isThrownBy(() -> policy.validateReservable(
                reservation, time, LocalDateTime.of(2026, 5, 6, 10, 0)
        ));
    }

    @DisplayName("현재 시간보다 이후 예약 시간일 때 예외가 발생하지 않음을 테스트합니다.")
    @Test
    void validateReservable_future_no_exception() {
        assertThatNoException().isThrownBy(() -> policy.validateReservable(
                reservation, time, LocalDateTime.of(2026, 5, 6, 9, 59)
        ));
    }
}
