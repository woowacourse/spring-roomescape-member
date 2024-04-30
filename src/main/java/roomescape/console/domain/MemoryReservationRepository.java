package roomescape.console.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;

public class MemoryReservationRepository implements ReservationRepository {
    private final Map<Long, Reservation> reservations;
    private final AtomicLong index;

    public MemoryReservationRepository() {
        this.reservations = new HashMap<>();
        this.index = new AtomicLong(1L);
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream()
                .sorted(Comparator.comparing(Reservation::getId))
                .toList();
    }

    @Override
    public Reservation findById(Long id) {
        return reservations.get(id);
    }

    @Override
    public Reservation create(Reservation reservation) {
        Reservation createdReservation = new Reservation(
                index.getAndIncrement(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                null
        );
        reservations.put(createdReservation.getId(), createdReservation);
        return createdReservation;
    }

    @Override
    public void removeById(Long id) {
        reservations.remove(id);
    }

    @Override
    public boolean hasDuplicateDateTimeReservation(Reservation reservation) {
        return false;
    }
}
