package roomescape.reservation.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;

@Repository
public class FakeReservationRepository implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> getAll() {
        return reservations.entrySet().stream()
                .map(entry -> Reservation.of(entry.getKey(), entry.getValue().getName(),
                        entry.getValue().getDate(), entry.getValue().getTime()))
                .toList();
    }

    @Override
    public Reservation put(final Reservation reservation) {
        Long id = index.getAndIncrement();
        reservations.put(id, reservation);
        return Reservation.of(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public boolean deleteById(final Long id) {
        return reservations.remove(id) != null;
    }
}

