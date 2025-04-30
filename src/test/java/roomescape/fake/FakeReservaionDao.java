package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationDao;
import roomescape.model.Reservation;

public class FakeReservaionDao implements ReservationDao {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public Reservation save(Reservation reservation) {
        Long id = this.id.getAndIncrement();
        Reservation saved = new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
        this.reservations.put(id, saved);

        return saved;
    }

    @Override
    public boolean deleteById(Long id) {
        return this.reservations.remove(id) != null;
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    //TODO 수정
    @Override
    public boolean isExistByTimeId(Long timeId) {
        return false;
    }

    @Override
    public boolean isExistByTimeIdAndDate(Long timeId, LocalDate date) {
        return false;
    }

    @Override
    public boolean isExistByThemeId(Long themeId) {
        return false;
    }
}
