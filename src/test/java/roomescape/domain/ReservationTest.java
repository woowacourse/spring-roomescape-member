package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @Test
    @DisplayName("성공 : id를 통해 동일한 예약인지 판별한다.")
    void checkSameReservation_Success() {
        Reservation reservation1 = new Reservation(
                1L,
                new Member("capy"),
                LocalDate.now().plusDays(1L),
                new ReservationTime(LocalTime.of(10, 0)),
                new Theme("capyTheme", "capyDescription", "capyThumbnail"));
        Reservation reservation2 = new Reservation(
                1L,
                new Member("capy"),
                LocalDate.now().plusDays(1L),
                new ReservationTime(LocalTime.of(10, 0)),
                new Theme("capyTheme", "capyDescription", "capyThumbnail"));

        assertThat(reservation1.isSameReservation(reservation2.getId())).isTrue();
    }

    @Test
    @DisplayName("실패 : id를 통해 동일한 예약인지 판별한다.")
    void checkSameReservation_Failure() {
        Reservation reservation1 = new Reservation(
                1L,
                new Member("capy"),
                LocalDate.now().plusDays(1L),
                new ReservationTime(LocalTime.of(10, 0)),
                new Theme("capyTheme", "capyDescription", "capyThumbnail"));
        Reservation reservation2 = new Reservation(
                2L,
                new Member("capy"),
                LocalDate.now().plusDays(1L),
                new ReservationTime(LocalTime.of(11, 0)),
                new Theme("capyTheme", "capyDescription", "capyThumbnail"));

        assertThat(reservation1.isSameReservation(reservation2.getId())).isFalse();
    }
}
