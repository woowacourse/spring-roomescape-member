package roomescape.service.fake;

import roomescape.dao.ReservationDao;
import roomescape.dao.row.ReservationRow;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeReservationDao implements ReservationDao {

    private final Map<Long, ReservationRow> store = new HashMap<>();
    private long sequence = 0L;


    @Override
    public List<ReservationRow> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<ReservationRow> findById(Long id) {
        ReservationRow reservation = store.get(id);

        if (reservation == null) {
            return Optional.empty();
        }

        return Optional.of(reservation);
    }

    @Override
    public ReservationRow create(ReservationRow reservation) {
        Long id = ++sequence;
        ReservationRow newReservation = new ReservationRow(
                id,
                reservation.name(),
                reservation.date(),
                reservation.timeRow(),
                reservation.themeRow());

        store.put(id, newReservation);
        return newReservation;

    }

    @Override
    public int delete(Long id) {
        ReservationRow remove = store.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsById(Long id) {
        return store.get(id) != null;
    }

    @Override
    public boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        return store.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.themeRow().id().equals(themeId)
                                && reservation.timeRow().id().equals(timeId)
                                && reservation.date().equals(date));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.themeRow().id().equals(themeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.timeRow().id().equals(timeId));
    }
}
