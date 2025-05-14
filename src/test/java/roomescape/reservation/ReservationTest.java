package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import roomescape.exception.ArgumentException;
import roomescape.exception.BadRequestException;
import roomescape.member.Member;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void 예약자명이_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                null,
                                LocalDate.now().plusDays(1),
                                ReservationTime.createWithoutId(LocalTime.of(9, 0)),
                                Theme.createWithoutId("themeName", "description", "thumb.jpg")
                        ))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    void 예약날짜가_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                Member.createWithoutId("짱구", "a", "1234", "USER"),
                                null,
                                ReservationTime.createWithoutId(LocalTime.of(9, 0)),
                                Theme.createWithoutId("themeName", "description", "thumb.jpg")
                        ))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 예약시간이_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                Member.createWithoutId("짱구", "a", "1234", "USER"),
                                LocalDate.of(2025, 1, 1),
                                null,
                                Theme.createWithoutId("themeName", "description", "thumb.jpg")
                        ))
                .isInstanceOf(ArgumentException.class);
    }

    @Test
    void 지나간_시간에_예약을_생성할_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                Member.createWithoutId("짱구", "a", "1234", "USER"),
                                LocalDate.of(2024, 1, 1),
                                new ReservationTime(1L, LocalTime.of(9, 0)),
                                Theme.createWithoutId("themeName", "description", "thumb.jpg")
                        ))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 테마가_null일_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(
                        () -> Reservation.createWithoutId(
                                Member.createWithoutId("짱구", "a", "1234", "USER"),
                                LocalDate.now().plusDays(1),
                                ReservationTime.createWithoutId(LocalTime.of(9, 0)),
                                null
                        ))
                .isInstanceOf(ArgumentException.class);

    }
}
