package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {
    @DisplayName("현재 시간보다 과거일 시 true를 반환한다.")
    @Test
    void isBeforeNow() {
        Reservation reservation = new Reservation(
                1L, LocalDate.now().minusDays(1),
                new Member(1L, "name1", "email1", "password1", Role.USER),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(reservation.isBeforeNow()).isTrue();
    }

    @DisplayName("현재 시간보다 미래일 시 false를 반환한다.")
    @Test
    void isNotBeforeNow() {
        Reservation reservation = new Reservation(
                1L, LocalDate.now().plusDays(1),
                new Member(1L, "name1", "email1", "password1", Role.USER),
                new ReservationTime(1L, LocalTime.of(10, 5)),
                new Theme(1L, "name1", "description1", "thumbnail1")
        );

        assertThat(reservation.isBeforeNow()).isFalse();
    }
}
