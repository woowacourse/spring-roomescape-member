package roomescape.repository.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {
    private final Reservations reservations = new Reservations();
    private final AtomicLong id = new AtomicLong(1);

    @Override
    public Reservation saveReservation(Reservation reservation) {
        Reservation createdReservation = Reservation.generateWithPrimaryKey(reservation, id.getAndIncrement());
        reservations.add(createdReservation);
        return createdReservation;
    }

    @Override
    public List<Reservation> readReservations() {
        return reservations.getReservations();
    }

    @Override
    public void deleteReservation(Long id) {
        reservations.remove(id);
    }

    private static class Reservations {
        private final List<Reservation> reservations;

        public Reservations() {
            this.reservations = new ArrayList<>();
        }

        public void add(Reservation reservation) {
            reservations.add(reservation);
        }

        public void remove(Long id) {
            boolean removed = reservations.removeIf(reservation -> reservation.isSameId(id));
            if (!removed) {
                throw new IllegalArgumentException("일치하는 ID의 예약을 찾을 수 없습니다.");
            }
        }

        public List<Reservation> getReservations() {
            return new ArrayList<>(reservations);
        }
    }
}
