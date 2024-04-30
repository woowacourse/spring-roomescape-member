package roomescape.console.dao;

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
    public ReservationTime findById(long id) {
        return inMemoryReservationTimeDb.selectById(id);
    }

    @Override
    public long save(ReservationTime reservationTime) {
        return inMemoryReservationTimeDb.insert(reservationTime.getStartAt());
    }

    @Override
    public boolean deleteById(long id) {
        boolean deleted = inMemoryReservationTimeDb.deleteById(id);
        inMemoryReservationDb.deleteByTimeId(id);
        return deleted;
    }
}
