package roomescape.testutil;

import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationMemoryRepository implements ReservationRepository {

    private final AtomicLong reservationId = new AtomicLong(1);
    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public Reservation save(final Reservation reservation) {
        final Long savedReservationId = reservationId.getAndIncrement();
        final Reservation savedReservation = new Reservation(savedReservationId, reservation.getName().getValue(), reservation.getDate(), reservation.getTime());
        reservations.add(savedReservation);
        return savedReservation;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public boolean deleteById(final Long id) {
        final Optional<Reservation> findReservation = reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
        return findReservation.map(reservations::remove).orElse(false);
    }
}
