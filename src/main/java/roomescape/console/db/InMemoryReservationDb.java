package roomescape.console.db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;

public class InMemoryReservationDb {
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1);

    public List<Reservation> selectAll() {
        return reservations.values()
                .stream()
                .toList();
    }

    public long insert(Reservation reservation) {
        long thisId = id.getAndIncrement();
        reservations.put(thisId, reservation.withId(thisId));
        return thisId;
    }

    public boolean deleteById(long id) {
        boolean exists = reservations.containsKey(id);
        if (exists) {
            reservations.remove(id);
        }
        return exists;
    }

    public void deleteByTimeId(long timeId) {
        reservations.entrySet()
                .stream()
                .filter(entry -> entry.getValue().hasSameTimeId(timeId))
                .forEach(entry -> reservations.remove(entry.getKey()));
    }
}
