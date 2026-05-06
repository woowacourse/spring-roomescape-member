package roomescape.time.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.NotFoundException;
import roomescape.time.domain.ReservationTime;

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
    public Long save(ReservationTime reservationTime) {
        Long id = idGenerator.getAndIncrement();
        ReservationTime saved = ReservationTime.of(id, reservationTime.startAt());
        store.put(id, saved);
        return id;
    }

    public List<ReservationTime> saveAll(List<ReservationTime> times) {
        List<ReservationTime> savedTimes = new ArrayList<>();
        for (ReservationTime time : times) {
            Long savedId = save(time);
            ReservationTime saved = ReservationTime.of(savedId, time.startAt());
            savedTimes.add(saved);
        }
        return savedTimes;
    }

    @Override
    public void delete(Long id) {
        ReservationTime removed = store.remove(id);
        if (removed == null) {
            throw new NotFoundException("예약 시간을 삭제할 수 없습니다.");
        }
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
