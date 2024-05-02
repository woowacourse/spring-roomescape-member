package roomescape.console.dao;

import java.time.LocalTime;
import java.util.List;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

public class InMemoryReservationTimeDao implements ReservationTimeDao {
    private final InMemoryReservationDb inMemoryReservationDb;
    private final InMemoryReservationTimeDb inMemoryReservationTimeDb;

    public InMemoryReservationTimeDao(InMemoryReservationDb inMemoryReservationDb,
                                      InMemoryReservationTimeDb inMemoryReservationTimeDb) {
        this.inMemoryReservationDb = inMemoryReservationDb;
        this.inMemoryReservationTimeDb = inMemoryReservationTimeDb;
    }

    @Override
    public List<ReservationTime> findAll() {
        return inMemoryReservationTimeDb.selectAll();
    }

    @Override
    public ReservationTime findById(Long id) {
        return inMemoryReservationTimeDb.selectById(id);
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        return inMemoryReservationTimeDb.selectAll().stream()
                .anyMatch(r -> r.getStartAt().equals(startAt));
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        long id = inMemoryReservationTimeDb.insert(reservationTime);
        return reservationTime.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleted = inMemoryReservationTimeDb.deleteById(id);
        inMemoryReservationDb.deleteByTimeId(id);
        return deleted;
    }
}
