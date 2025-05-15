package roomescape.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.impl.PastDateException;
import roomescape.exception.impl.ReservationBeforeOpenDayException;
import roomescape.exception.impl.ReservationThemeNotFoundException;
import roomescape.exception.impl.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationTest {

    @Test
    @DisplayName("현재로부터 7일 이후의 날짜를 예약시 예외가 발생한다.")
    void WhenReservationDateIsOver7NowThrowException() {
        // given
        LocalDate reservationDay = LocalDate.now().plusDays(8);
        Member member = Member.beforeMemberSave("레몬", "ywcsuwon@naver.com", "123");
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(reservationDay, member, reservationTime, theme))
                .isInstanceOf(ReservationBeforeOpenDayException.class);
    }

    @Test
    @DisplayName("현재 날짜 이전을 예약할 시 예외가 발생한다.")
    void WhenReservationDateIsBeforeNowThrowException() {
        // given
        LocalDate reservationDay = LocalDate.now().minusDays(1);
        Member member = Member.beforeMemberSave("레몬", "ywcsuwon@naver.com", "123");
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(reservationDay, member, reservationTime, theme))
                .isInstanceOf(PastDateException.class);
    }

    @Test
    @DisplayName("예약 할때 예약 시간이 없으면 예외가 발생한다.")
    void WhenReservationTimeIsNullThrowException() {
        // given
        Member member = Member.beforeMemberSave("레몬", "ywcsuwon@naver.com", "123");
        Theme theme = Theme.beforeSave("테마", "설명", "썸네일");
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(LocalDate.now(), member, null, theme))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    @DisplayName("예약 할때 테마가 없으면 예외가 발생한다.")
    void WhenThemeIsNullThrowException() {
        // given
        Member member = Member.beforeMemberSave("레몬", "ywcsuwon@naver.com", "123");
        ReservationTime reservationTime = ReservationTime.beforeSave(LocalTime.of(14, 0));
        // when
        // then
        Assertions.assertThatThrownBy(() -> Reservation.beforeSave(LocalDate.now(), member, reservationTime, null))
                .isInstanceOf(ReservationThemeNotFoundException.class);
    }
}


