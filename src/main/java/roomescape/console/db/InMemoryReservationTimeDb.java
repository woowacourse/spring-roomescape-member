package roomescape.console.db;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.ReservationTime;

public class InMemoryReservationTimeDb {
    private final Map<Long, ReservationTime> reservationTimes = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1);

    public List<ReservationTime> selectAll() {
        return reservationTimes.values()
                .stream()
                .toList();
    }

    public ReservationTime selectById(Long id) {
        if (reservationTimes.containsKey(id)) {
            return reservationTimes.get(id);
        }
        throw new EmptyResultDataAccessException("time id doesnt exist", 1);
    }

    public long insert(ReservationTime reservationTime) {
        long thisId = id.getAndIncrement();
        reservationTimes.put(thisId, reservationTime.withId(thisId));
        return thisId;
    }

    public boolean deleteById(long id) {
        boolean exists = reservationTimes.containsKey(id);
        if (exists) {
            reservationTimes.remove(id);
        }
        return exists;
    }
}
