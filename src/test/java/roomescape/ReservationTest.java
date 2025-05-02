package roomescape;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.impl.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationTest {

    @Test
    @DisplayName("현재로부터 7일 이후의 날짜를 예약시 예외가 발생한다.")
    void WhenReservationDateIsOver7NowThrowException() {
        // given
        LocalDate reservationDay = LocalDate.now().plusDays(8);
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave("레몬", reservationDay, reservationTime, theme))
                .isInstanceOf(ReservationBeforeOpenDayException.class);
    }

    @Test
    @DisplayName("현재 날짜 이전을 예약할 시 예외가 발생한다.")
    void WhenReservationDateIsBeforeNowThrowException() {
        // given
        LocalDate reservationDay = LocalDate.now().minusDays(1);
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave("레몬", reservationDay, reservationTime, theme))
                .isInstanceOf(PastDateException.class);
    }


    @ParameterizedTest
    @CsvSource({"abcdefghijk", "aaaaaaaaaaaaaa"})
    @DisplayName("예약자명이 10자를 넘어가면 예외가 발생한다")
    void WhenReservationNameOver10ThrowException(String name) {
        // given
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(name, LocalDate.now(), reservationTime, theme))
                .isInstanceOf(OverMaxNameLengthException.class);
    }

    @ParameterizedTest
    @CsvSource({"레몬123", "돔푸3456"})
    @DisplayName("예약자명에 숫자가 포함되면 예외가 발생한다")
    void WhenReservationNameContainNumberThrowException(String name) {
        // given
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(name, LocalDate.now(), reservationTime, theme))
                .isInstanceOf(NameContainsNumberException.class);
    }

    @Test
    @DisplayName("예약 할때 예약 시간이 없으면 예외가 발생한다.")
    void WhenReservationTimeIsNullThrowException() {
        // given
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave("레몬", LocalDate.now(), null, theme))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    @DisplayName("예약 할때 테마가 없으면 예외가 발생한다.")
    void WhenThemeIsNullThrowException() {
        // given
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave("레몬", LocalDate.now(), reservationTime, null))
                .isInstanceOf(ReservationThemeNotFoundException.class);
    }
}


