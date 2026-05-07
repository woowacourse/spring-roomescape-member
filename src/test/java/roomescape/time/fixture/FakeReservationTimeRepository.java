package roomescape.time.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> store = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReservationTime> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Long id = idGenerator.getAndIncrement();
        ReservationTime saved = ReservationTime.load(id, reservationTime.startAt());
        store.put(id, saved);
        return saved;
    }

    public List<ReservationTime> saveAll(List<ReservationTime> times) {
        List<ReservationTime> savedTimes = new ArrayList<>();
        for (ReservationTime time : times) {
            savedTimes.add(save(time));
        }
        return savedTimes;
    }

    @Override
    public boolean delete(Long id) {
        return store.remove(id) != null;
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return store.values().stream()
                .anyMatch(time -> time.startAt().equals(startAt));
    }

    @Override
    public List<ReservationTime> findAvailableByDateAndThemeId(LocalDate date, Long themeId) {
        return List.of();
    }

}
