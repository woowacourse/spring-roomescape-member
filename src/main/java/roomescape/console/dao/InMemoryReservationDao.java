package roomescape.console.dao;

import java.util.List;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.console.db.InMemoryReservationTimeDb;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class InMemoryReservationDao implements ReservationDao {
    private final InMemoryReservationDb inMemoryReservationDb;

    public InMemoryReservationDao(InMemoryReservationDb inMemoryReservationDb) {
        this.inMemoryReservationDb = inMemoryReservationDb;
    }

    @Override
    public List<Reservation> findAll() {
        return inMemoryReservationDb.selectAll();
    }

    @Override
    public Reservation save(Reservation reservation) {
        Long id = inMemoryReservationDb.insert(
                reservation.getName(), reservation.getDate().toString(), reservation.getTime());
        return reservation.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return inMemoryReservationDb.deleteById(id);
    }
}
