package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.ReservationTime;

class ReservationTest {

    @Test
    void 예약자명이_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                null,
                                LocalDate.of(2025, 1, 1),
                                ReservationTime.createWithoutId(LocalTime.of(9, 0))))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void 예약날짜가_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                "짱구",
                                null,
                                ReservationTime.createWithoutId(LocalTime.of(9, 0))))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 예약시간이_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                "짱구",
                                LocalDate.of(2025, 1, 1),
                                null))
                .isInstanceOf(NullPointerException.class);
    }

}