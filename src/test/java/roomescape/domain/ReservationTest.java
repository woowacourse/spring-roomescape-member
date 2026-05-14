package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.domain.ReservationException;

class ReservationTest {

    @Test
    void 현재보다_이전_날짜면_예외를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.of(2026, Month.MAY, 14, 11, 0);
        LocalDate pastDate = now.toLocalDate().minusDays(1);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(11, 0));
        Theme theme = new Theme(1L, "테마", "설명설명설명설명설명설명", "https://sdfsd.com");

        // when & then
        assertThatThrownBy(() -> Reservation.createFutureReservation(
                "예약자",
                pastDate,
                reservationTime,
                theme,
                now
        ))
                .isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.PAST_DATE_NOT_ALLOWED.getMessage());
    }

    @Test
    void 현재보다_이전_시간이면_예외를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.of(2026, Month.MAY, 14, 11, 0);
        LocalDate today = now.toLocalDate();
        ReservationTime pastTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명설명설명설명설명설명", "https://sdfsd.com");

        // when & then
        assertThatThrownBy(() -> Reservation.createFutureReservation(
                "예약자",
                today,
                pastTime,
                theme,
                now
        ))
                .isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.PAST_DATE_NOT_ALLOWED.getMessage());
    }
}
