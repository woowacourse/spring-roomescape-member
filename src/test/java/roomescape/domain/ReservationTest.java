/*
package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @DisplayName("테마가 같으면 true를 반환한다.")
    @Test
    void hasSameTheme() {
        Reservation firstReservation = new Reservation(
                1L, new Name("first"), LocalDate.of(2024, 10,5),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );
        Reservation secondReservation = new Reservation(
                2L, new Name("second"), LocalDate.of(2024, 10,5),
                new ReservationTime(2L, LocalTime.of(10, 6)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(firstReservation.hasSameTheme(secondReservation)).isTrue();
    }

    @DisplayName("테마가 다르면 false를 반환한다.")
    @Test
    void hasNotSameTheme() {
        Reservation firstReservation = new Reservation(
                1L, new Name("first"), LocalDate.of(2024, 10,5),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );
        Reservation secondReservation = new Reservation(
                2L, new Name("second"), LocalDate.of(2024, 10,5),
                new ReservationTime(2L, LocalTime.of(10, 6)),
                new Theme(2L, "name2", "description2", "thumbnail2")
        );

        assertThat(firstReservation.hasSameTheme(secondReservation)).isFalse();
    }

    @DisplayName("날짜가 같으면 true를 반환한다.")
    @Test
    void hasSameDateTime() {
        Reservation firstReservation = new Reservation(
                1L, new Name("first"), LocalDate.of(2024, 10,5),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );
        Reservation secondReservation = new Reservation(
                2L, new Name("second"), LocalDate.of(2024, 10,5),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(firstReservation.hasSameDateTime(secondReservation)).isTrue();
    }

    @DisplayName("날짜가 다르면 false를 반환한다.")
    @Test
    void hasNotSameDateTime() {
        Reservation firstReservation = new Reservation(
                1L, new Name("first"), LocalDate.of(2024, 10,5),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );
        Reservation secondReservation = new Reservation(
                2L, new Name("second"), LocalDate.of(2024, 10,5),
                new ReservationTime(2L, LocalTime.of(10, 6)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(firstReservation.hasSameDateTime(secondReservation)).isFalse();
    }

    @DisplayName("현재 시간보다 과거일 시 true를 반환한다.")
    @Test
    void isBeforeNow() {
        Reservation reservation = new Reservation(
                1L, new Name("first"), LocalDate.now().minusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(reservation.isBeforeNow()).isTrue();
    }

    @DisplayName("현재 시간보다 미래일 시 false를 반환한다.")
    @Test
    void isNotBeforeNow() {
        Reservation reservation = new Reservation(
                1L, new Name("first"), LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(reservation.isBeforeNow()).isFalse();
    }
}
*/
