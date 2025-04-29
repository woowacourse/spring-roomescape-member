package roomescape.repository;

import roomescape.domain.Reservation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReservationRepository implements ReservationRepository {

    private List<Reservation> reservations;
    AtomicLong reservationId = new AtomicLong(1);

    public FakeReservationRepository(final List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.id(), id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> save(final Reservation reservation) {
        Reservation newReservation = new Reservation(reservationId.getAndIncrement(), reservation.name(), reservation.date(), reservation.time());
        reservations.add(newReservation);
        return findById(newReservation.id());
    }

    @Override
    public int deleteById(long id) {
        Reservation deleteReservation = reservations.stream()
                .filter(reservation -> Objects.equals(reservation.id(), id))
                .findFirst().orElse(null);

        if (deleteReservation != null) {
            int affectedRows = (int) reservations.stream()
                    .filter(reservation -> Objects.equals(reservation.id(), deleteReservation.id()))
                    .count();

            reservations.remove(deleteReservation);
            return affectedRows;
        }
        return 0;
    }
}
