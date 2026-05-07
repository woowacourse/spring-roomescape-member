package roomescape.service.fake;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;

public class FakeReservationDao implements ReservationDao {

    private final Map<Long, Reservation> store = new HashMap<>();
    private long sequence = 0L;


    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        Reservation reservation = store.get(id);

        if (reservation == null) {
            return Optional.empty();
        }

        return Optional.of(reservation);
    }

    @Override
    public Reservation insert(Reservation reservation) {
        Long id = ++sequence;
        Reservation newReservation = new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme());

        store.put(id, newReservation);
        return newReservation;

    }

    @Override
    public int delete(Long id) {
        Reservation remove = store.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        return store.values()
                .stream()
                .anyMatch(reservation ->
                        reservation.getTheme().getId().equals(themeId)
                                && reservation.getTime().getId().equals(timeId)
                                && reservation.getDate().equals(date));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return store.values()
                .stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }
}
