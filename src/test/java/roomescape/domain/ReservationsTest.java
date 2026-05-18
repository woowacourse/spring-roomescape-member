package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReservationsTest {

    private final ReservationTime time = ReservationTime.of(1L, "10:00");
    private final Theme theme = Theme.of(1L, "공포", "desc", "url");

    @Test
    void 해당_날짜의_예약이_있으면_true를_반환한다() {
        ReservationDate date = ReservationDate.from("2027-06-01");
        Reservation reservation = Reservation.of(1L, "아이큐", date, time, theme);
        Reservations reservations = new Reservations(List.of(reservation));

        assertThat(reservations.hasReservationOn(date)).isTrue();
    }

    @Test
    void 해당_날짜의_예약이_없으면_false를_반환한다() {
        ReservationDate date = ReservationDate.from("2027-06-01");
        ReservationDate otherDate = ReservationDate.from("2027-06-02");
        Reservation reservation = Reservation.of(1L, "아이큐", date, time, theme);
        Reservations reservations = new Reservations(List.of(reservation));

        assertThat(reservations.hasReservationOn(otherDate)).isFalse();
    }

    @Test
    void 자신을_제외하고_해당_날짜의_예약이_있으면_true를_반환한다() {
        ReservationDate date = ReservationDate.from("2027-06-01");
        Reservation reservation1 = Reservation.of(1L, "아이큐", date, time, theme);
        Reservation reservation2 = Reservation.of(2L, "브라운", date, time, theme);
        Reservations reservations = new Reservations(List.of(reservation1, reservation2));

        assertThat(reservations.hasReservationOnExcluding(date, 1L)).isTrue();
    }

    @Test
    void 자신을_제외하면_해당_날짜의_예약이_없으면_false를_반환한다() {
        ReservationDate date = ReservationDate.from("2027-06-01");
        Reservation reservation = Reservation.of(1L, "아이큐", date, time, theme);
        Reservations reservations = new Reservations(List.of(reservation));

        assertThat(reservations.hasReservationOnExcluding(date, 1L)).isFalse();
    }
}
