package roomescape.fixture;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.repository.reservationtime.ReservationTimeRepository;

public class ReservationTimeRepositoryStub implements ReservationTimeRepository {
    private final Map<Long, ReservationTime> times = new HashMap<>();
    private Long id = 1L;

    @Override
    public ReservationTime add(ReservationTime time) {
        ReservationTime newTime = new ReservationTime(id, time.getStartAt());
        times.put(id++, newTime);
        return newTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return times.values().stream().toList();
    }

    @Override
    public int deleteById(Long id) {
        ReservationTime removedTime = times.remove(id);
        if (removedTime == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public Optional<ReservationTime> findById(Long timeId) {
        ReservationTime time = times.get(timeId);
        if (time == null) {
            return Optional.empty();
        }
        return Optional.of(time);
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return times.values().stream()
                .anyMatch(time -> time.getStartAt() == startAt);
    }
}
