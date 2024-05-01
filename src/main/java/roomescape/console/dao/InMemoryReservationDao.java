package roomescape.console.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.console.db.InMemoryReservationDb;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

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
    public boolean existsByDateTime(LocalDate date, Long timeId) {
        return inMemoryReservationDb.selectAll()
                .stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .anyMatch(reservation -> reservation.hasSameTimeId(timeId));
    }

    @Override
    public Reservation save(Reservation reservation) {
        Long id = inMemoryReservationDb.insert(
                reservation.getName(), reservation.getDate().toString(), reservation.getTime(), reservation.getTheme());
        return reservation.withId(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return inMemoryReservationDb.deleteById(id);
    }
}
