package roomescape.repository.fake;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public List<Reservation> findAll(int limit, int offset) {
        return store.values().stream()
                .sorted(Comparator.comparing(Reservation::getId))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    public Reservation findById(Long id) {
        return store.get(id);
    }

    @Override
    public Long save(Reservation reservation) {
        Long id = nextId++;
        store.put(id, reservation.withId(id));
        return id;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        return store.values().stream()
                .filter(r -> r.getTheme().getId().equals(themeId))
                .filter(r -> r.getDate().equals(date))
                .map(r -> r.getTime().getId())
                .toList();
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return store.values().stream()
                .anyMatch(r -> r.getDate().equals(date)
                        && r.getTime().getId().equals(timeId)
                        && r.getTheme().getId().equals(themeId));
    }

    Collection<Reservation> all() {
        return Collections.unmodifiableCollection(store.values());
    }
}