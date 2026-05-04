package roomescape.domain.time.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.domain.time.entity.Time;

public class FakeTimeRepository implements TimeRepository {

    private final List<Time> times = new ArrayList<>();
    private Long sequence = 1L;

    @Override
    public Time save(Time time) {
        Time savedTime = Time.reconstruct(sequence++, time.getStartAt());
        times.add(savedTime);
        return savedTime;
    }

    @Override
    public List<Time> findAllTimes() {
        return times;
    }

    @Override
    public Time findTimeById(Long id) {
        return times.stream()
            .filter(time -> Objects.equals(time.getId(), id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteTimeById(Long id) {
        times.removeIf(time -> Objects.equals(time.getId(), id));
    }
}
