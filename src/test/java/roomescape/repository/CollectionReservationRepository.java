package roomescape.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;

public class CollectionReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong atomicLong;


    public CollectionReservationRepository() {
        this.reservations = new ArrayList<>();
        this.atomicLong = new AtomicLong(0);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saved = new Reservation(atomicLong.incrementAndGet(), reservation);
        reservations.add(saved);
        return saved;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.stream()
                .sorted()
                .toList();
    }

    @Override
    public void delete(long id) {
        reservations.stream()
                .filter(reservation -> reservation.hasSameId(id))
                .findAny()
                .ifPresent(reservations::remove);
    }
}
