package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservationtime.ReservationTime;

public class ConsoleReservationTimeDao implements ReservationTimeDao {

    private static final int INITIAL_ID_VALUE = 1;

    private final AtomicLong reservationTimeId = new AtomicLong(INITIAL_ID_VALUE);
    private final Map<Long, ReservationTime> store = new ConcurrentHashMap<>();

    @Override
    public List<ReservationTime> readAll() {
        return store.values().stream().toList();
    }

    @Override
    public ReservationTime readById(long id) {
        return store.get(id);
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        long id = reservationTimeId.getAndIncrement();
        ReservationTime newReservationTime = new ReservationTime(
                id,
                reservationTime.getStartAt()
        );
        store.put(id, newReservationTime);
        return newReservationTime;
    }

    @Override
    public Boolean exist(long id) {
        return store.containsKey(id);
    }

    @Override
    public void delete(long id) {
        store.remove(id);
    }
}
