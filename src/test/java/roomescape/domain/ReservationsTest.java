package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationsTest {

    private Reservations reservations;

    @BeforeEach
    void init() {
        reservations = new Reservations();
    }

    @DisplayName("예약을 한 번 저장할 수 있어야 한다.")
    @Test
    void save_once() {
        //given
        Reservation reservation = new Reservation(
            1,
            new Person(1, "james"),
            new ReservationTime(LocalDateTime.of(2025, 4, 17, 11, 30)));

        //when
        reservations.save(reservation);

        //then
        assertThat(reservations.getReservations().size()).isEqualTo(1);
        assertThat(reservations.getReservations()).contains(reservation);
    }

    @DisplayName("예약을 여러 번 저장할 수 있어야 한다.")
    @Test
    void save() {
        //given
        Reservation reservation1 = new Reservation(
            1,
            new Person(1, "james"),
            new ReservationTime(LocalDateTime.of(2025, 4, 17, 11, 30)));

        Reservation reservation2 = new Reservation(
            2,
            new Person(2, "pobi"),
            new ReservationTime(LocalDateTime.of(2025, 4, 18, 12, 30)));

        //when
        reservations.save(reservation1);
        reservations.save(reservation2);

        //then
        assertThat(reservations.getReservations().size()).isEqualTo(2);
        assertThat(reservations.getReservations()).containsExactlyInAnyOrder(reservation1,
            reservation2);
    }

    @DisplayName("존재하지 않는 예약ID가 주어졌을 경우, 예외가 발생해야 한다.")
    @Test
    void given_not_exist_id_then_throw_exception() {
        assertThatThrownBy(() -> reservations.deleteById(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 예약번호 입니다.");
    }

    @DisplayName("예약ID가 주어졌을 경우, 해당 예약ID가 존재한다면 예약을 삭제해야 한다.")
    @Test
    void given_valid_id_then_delete_reservation() {
        //given
        Reservation reservation1 = new Reservation(
            1,
            new Person(1, "james"),
            new ReservationTime(LocalDateTime.of(2025, 4, 17, 11, 30)));
        Reservation reservation2 = new Reservation(
            2,
            new Person(2, "pobi"),
            new ReservationTime(LocalDateTime.of(2025, 4, 18, 12, 30)));
        reservations.save(reservation1);
        reservations.save(reservation2);

        //when
        reservations.deleteById(2);

        //then
        assertThat(reservations.getReservations().size()).isEqualTo(1);
        assertThat(reservations.getReservations()).doesNotContain(reservation2);
    }
}
