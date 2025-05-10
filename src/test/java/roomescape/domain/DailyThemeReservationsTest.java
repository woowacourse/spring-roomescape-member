package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.BusinessRuleViolationException;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.DailyThemeReservations;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

class DailyThemeReservationsTest {

    @Test
    void 생성시_같은_테마와_같은_날짜가_아닌_예약이_있으면_예외가_발생한다() {
        //given
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        new Member(1L, "test1", "email1", "password", Role.NORMAL),
                        LocalDate.of(2025, 5, 8),
                        new ReservationTime(1L, LocalTime.of(13, 1)),
                        new Theme(1L, "theme", "test", "url")),
                new Reservation(
                        2L,
                        new Member(1L, "test1", "email1", "password", Role.NORMAL),
                        LocalDate.of(2025, 5, 8),
                        new ReservationTime(1L, LocalTime.of(13, 3)),
                        new Theme(2L, "theme", "test", "url"))
        );

        //when & then
        assertThatThrownBy(() -> new DailyThemeReservations(reservations, 1L, LocalDate.of(2025, 5, 8)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("특정 테마, 특정 날짜에 속한 예약이 아닙니다.");
    }

    @Test
    void 예약된_시간을_계산할_수_있다() {
        //given
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        new Member(1L, "test1", "email1", "password", Role.NORMAL),
                        LocalDate.of(2025, 5, 8),
                        new ReservationTime(1L, LocalTime.of(13, 0)),
                        new Theme(1L, "theme", "test", "url")),
                new Reservation(
                        2L,
                        new Member(1L, "test1", "email1", "password", Role.NORMAL),
                        LocalDate.of(2025, 5, 8),
                        new ReservationTime(2L, LocalTime.of(14, 0)),
                        new Theme(1L, "theme", "test", "url"))
        );
        DailyThemeReservations dailyThemeReservations = new DailyThemeReservations(reservations, 1L,
                LocalDate.of(2025, 5, 8));

        //when
        Set<ReservationTime> reservationTimes = dailyThemeReservations.calculateBookedTimes();

        //then
        assertThat(reservationTimes).isEqualTo(Set.of(
                new ReservationTime(1L, LocalTime.of(13, 0)),
                new ReservationTime(2L, LocalTime.of(14, 0))
        ));
    }
}