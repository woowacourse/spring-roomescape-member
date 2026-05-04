package roomescape.domain.reservation.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.entity.Reservation;

public class FakeReservationRepository implements ReservationRepository {

    private final AtomicLong id = new AtomicLong(0);

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findAllReservations() {
        return new ArrayList<>(reservations);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation savedReservation = Reservation.reconstruct(id.addAndGet(1), reservation.getName(),
            reservation.getDate(),
            reservation.getTime());
        reservations.add(savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteReservationById(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }
}
