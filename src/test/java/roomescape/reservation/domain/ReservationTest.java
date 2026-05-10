package roomescape.reservation.domain;


import java.time.LocalDate;
import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    @Test
    void 식별자가_같은_경우_동등한_객체로_판단한다() {
        // given
        ReservationTime reservationTimeA = new ReservationTime(1L, LocalTime.of(10,1));
        ReservationTime reservationTimeB = new ReservationTime(2L, LocalTime.of(10,2));
        Theme themeA = new Theme(1L, "A", "hello", "/resources/image/...");
        Theme themeB = new Theme(2L, "B", "hello", "/resources/image/...");

        // when
        Reservation reservationA = new Reservation(1L, "name1", LocalDate.of(2026,1,1), reservationTimeA, themeA);
        Reservation reservationB = new Reservation(1L, "name2", LocalDate.of(2026,1,2), reservationTimeB, themeB);

        //then
        Assertions.assertThat(reservationA).isEqualTo(reservationB);
    }

    @Test
    void 이름이_50자를_넘는_경우_예외가_발생한다() {
        //given
        String name = "a".repeat(51);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        LocalDate now = LocalDate.now();

        Theme theme = new Theme(null, "hello", "world", "/resources/image/...");

        //when & then
        Assertions.assertThatThrownBy(() -> new Reservation(1L, name, now, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
