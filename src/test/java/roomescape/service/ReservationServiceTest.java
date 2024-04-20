package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;

class ReservationServiceTest {
    ReservationService reservationService = new ReservationService(new FakeReservationRepository(),
            new FakeReservationTimeRepository());

    @DisplayName("모든 예약 시간을 반환한다")
    @Test
    void should_return_all_reservation_times() {
        List<Reservation> reservations = reservationService.findAllReservations();
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        reservationService.addReservation(
                new ReservationRequest(LocalDate.of(2024, 9, 1), "네오", 1));
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_times() {
        reservationService.deleteReservation(1);
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(1);
    }

    class FakeReservationRepository implements ReservationRepository {

        private List<Reservation> reservations = new ArrayList<>(List.of(
                new Reservation(1, "브라운", LocalDate.of(2023, 8, 5),
                        new ReservationTime(1, LocalTime.of(10, 0))),
                new Reservation(1, "리사", LocalDate.of(2023, 8, 1),
                        new ReservationTime(2, LocalTime.of(11, 0)))));

        @Override
        public List<Reservation> getAllReservations() {
            return reservations;
        }

        @Override
        public Reservation addReservation(Reservation reservation) {
            reservations.add(reservation);
            return reservation;
        }

        @Override
        public long deleteReservation(long id) {
            Reservation foundReservation = reservations.stream()
                    .filter(reservation -> reservation.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("아이디가 존재하지 않습니다."));
            reservations.remove(foundReservation);
            return id;
        }
    }

}
