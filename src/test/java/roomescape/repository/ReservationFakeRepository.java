package roomescape.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Reservation;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public Optional<Reservation> findById(final long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public long save(Reservation reservation) {
        var id = index.getAndIncrement();
        var created = new Reservation(
            id,
            reservation.name(),
            reservation.date(),
            reservation.timeSlot()
        );
        reservations.put(id, created);
        return id;
    }

    public boolean removeById(long id) {
        Reservation removed = reservations.remove(id);
        return removed != null;
    }

    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }
}
